package org.hana.wooahhanaapi.domain.plan.repository;

import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {
    @Query("SELECT p FROM PlanEntity p WHERE p.communityId = :communityId AND p.endDate < CURRENT_TIMESTAMP")
    List<PlanEntity> findCompletedByCommunityId(@Param("communityId") UUID communityId);

    @Query("SELECT p FROM PlanEntity p WHERE p.communityId = :communityId AND p.endDate > CURRENT_TIMESTAMP")
    List<PlanEntity> findAllByCommunityId(@Param("communityId") UUID communityId);

    @Query("SELECT p FROM PlanEntity p " + "WHERE p.communityId = :communityId " + "AND (p.startDate <= :toDate AND p.endDate >= :fromDate)")
    List<PlanEntity> findPlansInPeriod(
            @Param("communityId") UUID communityId, @Param("fromDate") LocalDateTime fromDate, @Param("toDate") LocalDateTime toDate
    );
}
