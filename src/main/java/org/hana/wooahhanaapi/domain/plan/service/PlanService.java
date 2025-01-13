package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.UpdatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidPostDataException;
import org.hana.wooahhanaapi.domain.plan.exception.LogicalPlanDataException;
import org.hana.wooahhanaapi.domain.plan.mapper.PlanMapper;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;

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

    @Transactional
    public void deletePlan(String planId) {
        PlanEntity plan = planRepository.findById(UUID.fromString(planId))
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));
        planRepository.delete(plan);
    }

    public List<GetPlansResponseDto> getPlans(UUID communityId) {
        return planRepository.findAllByCommunityId(communityId)
                .stream()
                .map(PlanMapper::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePlan(UUID planId, UpdatePlanRequestDto requestDto) {
        PlanEntity plan = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));

        PlanEntity updatedPlan = PlanEntity.builder()
                .id(plan.getId())
                .communityId(plan.getCommunityId())
                .title(requestDto.getTitle() != null ? requestDto.getTitle() : plan.getTitle())
                .startDate(requestDto.getStartDate() != null ? requestDto.getStartDate() : plan.getStartDate())
                .endDate(requestDto.getEndDate() != null ? requestDto.getEndDate() : plan.getEndDate())
                .category(requestDto.getCategory() != null ? requestDto.getCategory() : plan.getCategory())
                .locations(requestDto.getLocations() != null ? requestDto.getLocations() : plan.getLocations())
                .memberIds(requestDto.getMemberIds() != null ? requestDto.getMemberIds() : plan.getMemberIds())
                .build();

        if (updatedPlan.getStartDate() != null && updatedPlan.getEndDate() != null) {
            if (updatedPlan.getStartDate().isAfter(updatedPlan.getEndDate())) {
                throw new LogicalPlanDataException("종료일은 시작일 이후여야 합니다.");
            }
        }
        planRepository.save(updatedPlan);
    }
}
