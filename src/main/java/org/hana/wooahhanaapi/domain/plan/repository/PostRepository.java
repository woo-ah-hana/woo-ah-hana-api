package org.hana.wooahhanaapi.domain.plan.repository;

import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostRepository extends JpaRepository<PostEntity, UUID> {
}
