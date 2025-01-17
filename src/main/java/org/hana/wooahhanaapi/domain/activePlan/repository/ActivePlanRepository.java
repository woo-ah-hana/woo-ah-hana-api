package org.hana.wooahhanaapi.domain.activePlan.repository;

import org.hana.wooahhanaapi.domain.activePlan.entity.ActivePlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivePlanRepository extends JpaRepository<ActivePlanEntity, UUID> {
    List<ActivePlanEntity> findByPlanId(UUID planId);
}
