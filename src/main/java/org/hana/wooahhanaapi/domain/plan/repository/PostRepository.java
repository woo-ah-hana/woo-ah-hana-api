package org.hana.wooahhanaapi.domain.plan.repository;

import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    @Query("SELECT p FROM PostEntity p WHERE p.plan.id = :planId")
    List<PostEntity> findCompletedByPlanId(@Param("planId") UUID planId);
    void deleteByPlan(PlanEntity plan);
    PostEntity findFirstByPlan(PlanEntity plan);
}
