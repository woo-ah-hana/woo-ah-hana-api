package org.hana.wooahhanaapi.domain.community.repository;

import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository extends JpaRepository<MembershipEntity, UUID> {
    List<MembershipEntity> findAllByMemberAndCommunity(MemberEntity member, CommunityEntity community);
    @Query("SELECT m.member FROM MembershipEntity m WHERE m.community.id = :communityId")
    List<MemberEntity> findMembersByCommunityId(@Param("communityId") UUID communityId);
    @Query("SELECT m.community FROM MembershipEntity m WHERE m.member.id = :memberId")
    List<CommunityEntity> findCommunitiesByMemberId(@Param("memberId") UUID memberId);
    boolean existsByMemberAndCommunity(MemberEntity member, CommunityEntity community);
    void deleteByMemberAndCommunity(MemberEntity member, CommunityEntity community);
}
