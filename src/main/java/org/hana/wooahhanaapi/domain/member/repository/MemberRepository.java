package org.hana.wooahhanaapi.domain.member.repository;

import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<MemberEntity, UUID> {
    Optional<MemberEntity> findByUsername(String username);
    Optional<MemberEntity> findByAccountNumber(String accountNumber);
    MemberEntity findAllByUsername(String username);
    UUID getIdByUsername(String username);
}
