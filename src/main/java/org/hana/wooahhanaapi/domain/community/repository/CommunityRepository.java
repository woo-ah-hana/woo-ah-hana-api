package org.hana.wooahhanaapi.domain.community.repository;

import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CommunityRepository extends JpaRepository<CommunityEntity, UUID> {
    List<CommunityEntity> findAllByManagerId(UUID managerId);
    Optional<CommunityEntity> findByName(String name);
    Optional<CommunityEntity> findByAccountNumber(String accountNumber);
}
