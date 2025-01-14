package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.UpdatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
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

    public Plan getPlanDetail(UUID planId) {
        PlanEntity planEntity = planRepository.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("해당 plan을 찾을 수 없습니다."));

        return PlanMapper.mapEntityToDomain(planEntity);


    }

    public List<GetPlansResponseDto> getPlans(UUID communityId) {
        return planRepository.findAllByCommunityId(communityId)
                .stream()
                .map(PlanMapper::mapPlansEntityToDto)
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
}
