package org.hana.wooahhanaapi.domain.community.repository;

import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<MembershipEntity, UUID> {
    List<MembershipEntity> findAllByMemberAndCommunity(MemberEntity member, CommunityEntity community);
}
