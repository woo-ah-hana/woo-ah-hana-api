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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivePlanService {
    private final ActivePlanRepository activePlanRepository;
    // TODO: 스웨거에서 id가 생성됨
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

    @Transactional
    public List<UUID> saveActivePlans(List<CreateActivePlanRequestDto> dtoList) {
        activePlanRepository.deleteAll();
        List<ActivePlanEntity> result = new ArrayList<>();
        for (CreateActivePlanRequestDto dto : dtoList) {
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
            result.add(ActivePlanMapper.mapDomainToEntity(activePlan));
        }
        return this.activePlanRepository.saveAll(result).stream().map(ActivePlanEntity::getId).collect(Collectors.toList());
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
