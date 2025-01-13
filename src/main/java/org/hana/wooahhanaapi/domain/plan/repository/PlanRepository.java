package org.hana.wooahhanaapi.domain.plan.repository;

import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<PlanEntity, UUID> {
    List<PlanEntity> findAllByCommunityId(UUID communityId);
}
