package org.hana.wooahhanaapi.domain.plan.mapper;

import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPostResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;

import java.util.stream.Collectors;

public class PlanMapper {
    public static PlanEntity mapDomainToEntity(Plan plan) {
        return PlanEntity.builder()
                .id(plan.getId())
                .communityId(plan.getCommunityId())
                .title(plan.getTitle())
                .startDate(plan.getStartDate())
                .endDate(plan.getEndDate())
                .category(plan.getCategory())
                .locations(plan.getLocations())
                .memberIds(plan.getMemberIds())
                .build();
    }

    public static Plan mapEntityToDomain(PlanEntity entity){
        return Plan.create(
                entity.getId(),
                entity.getCommunityId(),
                entity.getTitle(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCategory(),
                entity.getLocations(),
                entity.getMemberIds()
        );
    }

    public static GetPlansResponseDto mapPlansEntityToDto(PlanEntity entity, MemberRepository memberRepository) {
        MemberNameResolver resolver = new MemberNameResolver(memberRepository);

        return GetPlansResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .category(entity.getCategory())
                .locations(entity.getLocations())
                .memberIds(entity.getMemberIds())
                .memberNames(resolver.resolveMemberNames(entity.getMemberIds()))
                .build();
    }


    public static GetPostResponseDto mapPostsEntityToDto(PostEntity entity){
        return GetPostResponseDto.builder()
                .id(entity.getId())
                .memberName(entity.getMember().getName())
                .imageUrl(entity.getImageUrl())
                .description(entity.getDescription())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
