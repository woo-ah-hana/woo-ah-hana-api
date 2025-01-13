package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.mapper.PlanMapper;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;


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
}
