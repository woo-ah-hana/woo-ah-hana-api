package org.hana.wooahhanaapi.domain.plan.mapper;

import jakarta.persistence.Id;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.ListPlanResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;

public class PlanMapper {
    public static PlanEntity mapDomainToEntity(Plan plan) {
        return PlanEntity.builder()
                .communityId(plan.getCommunityId())
                .title(plan.getTitle())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .category(plan.getCategory())
                .locations(plan.getLocations())
                .memberIds(plan.getMemberIds())
                .build();
    }

    public static ListPlanResponseDto mapEntityToDto(PlanEntity entity){
        return ListPlanResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .category(entity.getCategory())
                .locations(entity.getLocations())
                .build();
    }
}
