package org.hana.wooahhanaapi.domain.account.repository;

import org.hana.wooahhanaapi.domain.account.entity.AccountValidationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AccountValidationRepository extends JpaRepository<AccountValidationEntity, UUID> {
    @Query("select a from AccountValidationEntity a where a.accountNumber = :accountNumber")
    AccountValidationEntity findByAccountNumber(String accountNumber);
}
