package org.hana.wooahhanaapi.domain.activePlan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.activePlan.domain.ActivePlan;
import org.hana.wooahhanaapi.domain.activePlan.dto.CreateActivePlanRequestDto;
import org.hana.wooahhanaapi.domain.activePlan.entity.ActivePlanEntity;
import org.hana.wooahhanaapi.domain.activePlan.mapper.ActivePlanMapper;
import org.hana.wooahhanaapi.domain.activePlan.repository.ActivePlanRepository;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivePlanService {
    private final ActivePlanRepository activePlanRepository;

    public UUID createActivePlan(CreateActivePlanRequestDto dto) {
        ActivePlan activePlan = ActivePlan.create(
                null,
                dto.getPlanId(),
                dto.getDate(),
                dto.getSchedule(),
                dto.getTime(),
                dto.getDescription(),
                dto.getAddress(),
                dto.getLink(),
                dto.getMapx(),
                dto.getMapy()
        );
        return activePlanRepository.save(ActivePlanMapper.mapDomainToEntity(activePlan)).getId();
    }
    public List<ActivePlan> getActivePlan(UUID planId) {
        return activePlanRepository.findByPlanId(planId)
                .stream()
                .map(ActivePlanMapper::mapEntityToDomain)
                .collect(Collectors.toList());
    }
    @Transactional
    public void deleteActivePlan(UUID planId) {
        List<ActivePlanEntity> activePlan = activePlanRepository.findByPlanId(planId);
        if (activePlan.isEmpty()) {
            throw new EntityNotFoundException("해당 plan을 찾을 수 없습니다.");
        }
        activePlanRepository.deleteAll(activePlan);
    }
}
