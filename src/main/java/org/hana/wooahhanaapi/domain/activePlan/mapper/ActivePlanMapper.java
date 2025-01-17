
package org.hana.wooahhanaapi.domain.activePlan.mapper;

import org.hana.wooahhanaapi.domain.activePlan.domain.ActivePlan;
import org.hana.wooahhanaapi.domain.activePlan.dto.GetActivePlanResponseDto;
import org.hana.wooahhanaapi.domain.activePlan.entity.ActivePlanEntity;

public class ActivePlanMapper {
    public static ActivePlanEntity mapDomainToEntity(ActivePlan activePlan) {
        return ActivePlanEntity.builder()
                .id(activePlan.getId())
                .planId(activePlan.getPlanId())
                .date(activePlan.getDate())
                .schedule(activePlan.getSchedule())
                .time(activePlan.getTime())
                .description(activePlan.getDescription())
                .address(activePlan.getAddress())
                .link(activePlan.getLink())
                .mapx(activePlan.getMapx())
                .mapy(activePlan.getMapy())
                .build();
    }

    public static ActivePlan mapEntityToDomain(ActivePlanEntity entity){
        return ActivePlan.create(
                entity.getId(),
                entity.getPlanId(),
                entity.getDate(),
                entity.getSchedule(),
                entity.getTime(),
                entity.getDescription(),
                entity.getAddress(),
                entity.getLink(),
                entity.getMapx(),
                entity.getMapy()
        );
    }

    public static GetActivePlanResponseDto mapPostsEntityToDto(ActivePlanEntity entity){
        return GetActivePlanResponseDto.builder()
                .Id(entity.getId())
                .planId(entity.getPlanId())
                .date(entity.getDate())
                .time(entity.getTime())
                .description(entity.getDescription())
                .address(entity.getAddress())
                .link(entity.getLink())
                .mapx(entity.getMapx())
                .mapy(entity.getMapy())
                .build();
    }
}
