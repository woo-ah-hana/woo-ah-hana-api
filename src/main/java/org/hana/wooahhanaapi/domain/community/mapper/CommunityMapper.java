package org.hana.wooahhanaapi.domain.community.mapper;

import org.hana.wooahhanaapi.domain.community.domain.Community;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;

public class CommunityMapper {

    public static CommunityEntity mapDomainToEntity(Community community) {
        return CommunityEntity.builder()
                .managerId(community.getManagerId())
                .name(community.getName())
                .accountNumber(community.getAccountNumber())
                .credits(community.getCredits())
                .fee(community.getFee())
                .feePeriod(community.getFeePeriod())
                .build();
    }

    public static Community mapEntityToDomain(CommunityEntity communityEntity) {
        return Community.create(
            communityEntity.getId(), communityEntity.getManagerId(), communityEntity.getName(),
                communityEntity.getAccountNumber(), communityEntity.getCredits(), communityEntity.getFee(),
                communityEntity.getFeePeriod()
        );

    }
}
