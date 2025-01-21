package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespListDto;
import org.hana.wooahhanaapi.domain.community.dto.CommunityTrsfRecordRespDto;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetReceiptResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.UpdatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.mapper.PlanMapper;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final CommunityRepository communityRepository;
    private final AccountTransferRecordPort accountTransferRecordPort;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;

    public UUID createPlan(CreatePlanRequestDto dto) {
        Plan plan = Plan.create(
                null,
                dto.getCommunityId(),
                dto.getTitle(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCategory(),
                dto.getLocations(),
                dto.getMemberIds()
        );
        return planRepository.save(PlanMapper.mapDomainToEntity(plan)).getId();
    }

    public List<String> getMembers(UUID communityId) {
        List<MemberEntity> foundMembers = membershipRepository.findMembersByCommunityId(communityId);
        return foundMembers.stream().map(MemberEntity::getName).collect(Collectors.toList());
    }

    @Transactional
    public void deletePlan(String planId) {
        PlanEntity plan = planRepository.findById(UUID.fromString(planId))
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));
        planRepository.delete(plan);
    }

    public GetPlansResponseDto getPlanDetail(UUID planId) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));

        return PlanMapper.mapPlansEntityToDto(planEntity, memberRepository);


    }

    public List<Plan> getPlans(UUID communityId) {
        return planRepository.findAllByCommunityId(communityId)
                .stream()
                .map(PlanMapper::mapEntityToDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePlan(UUID planId, UpdatePlanRequestDto dto) {

        PlanEntity existingPlanEntity = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));

        Plan plan = Plan.update(
                planId,
                existingPlanEntity.getCommunityId(),
                dto.getTitle() != null ? dto.getTitle() : existingPlanEntity.getTitle(),
                dto.getStartDate() != null ? dto.getStartDate() : existingPlanEntity.getStartDate(),
                dto.getEndDate() != null ? dto.getEndDate() : existingPlanEntity.getEndDate(),
                dto.getCategory() != null ? dto.getCategory() : existingPlanEntity.getCategory(),
                dto.getLocations() != null ? dto.getLocations() : existingPlanEntity.getLocations(),
                dto.getMemberIds() != null ? dto.getMemberIds() : existingPlanEntity.getMemberIds()
        );

        planRepository.save(PlanMapper.mapDomainToEntity(plan));
    }

    public List<Plan> getCompletedPlans(UUID communityId) {
        return planRepository.findCompletedByCommunityId(communityId)
                .stream()
                .map(PlanMapper::mapEntityToDomain)
                .collect(Collectors.toList());
    }

    public GetReceiptResponseDto getPlanReceipt(UUID planId) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));

        CommunityEntity foundCommunity = communityRepository.findById(plan.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("커뮤니티를 찾을 수 없습니다."));

        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber(foundCommunity.getAccountNumber())
                .fromDate(plan.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toDate(plan.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();

        List<AccountTransferRecordRespListDto> resultData = accountTransferRecordPort.getTransferRecord(reqDto)
                .getData().getResList();

        List<CommunityTrsfRecordRespDto> records = resultData.stream()
                .map(record -> new CommunityTrsfRecordRespDto(
                        record.getTranDate(),
                        record.getTranTime(),
                        record.getInoutType(),
                        record.getTranType(),
                        record.getPrintContent(),
                        record.getTranAmt(),
                        record.getAfterBalanceAmt(),
                        record.getBranchName()
                )).collect(Collectors.toList());

        long totalAmt = records.stream()
                .filter(record -> "출금".equals(record.getInoutType()))
                .mapToLong(record -> Long.parseLong(record.getTranAmt()))
                .sum();

        long perAmt = (plan.getMemberIds().isEmpty()) ? 0 : totalAmt / plan.getMemberIds().size();

        return GetReceiptResponseDto.builder()
                .records(records)
                .totalAmt(totalAmt)
                .perAmt(perAmt)
                .build();
    }
}
