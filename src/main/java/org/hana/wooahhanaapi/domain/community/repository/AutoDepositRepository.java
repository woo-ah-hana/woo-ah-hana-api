package org.hana.wooahhanaapi.domain.community.repository;

import org.hana.wooahhanaapi.domain.community.domain.AutoDeposit;
import org.hana.wooahhanaapi.domain.community.entity.AutoDepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AutoDepositRepository extends JpaRepository<AutoDepositEntity, UUID> {
    List<AutoDepositEntity> findALlByDepositDay(int depositDay);

    AutoDepositEntity findByCommunityAccNumAndAndMemberAccNumAndMemberBankTranId(String accountNumber, String accountNumber1, String bankTranId);
  
    boolean existsByCommunityAccNumAndMemberAccNum(String communityAccNum, String memberAccNum);

    List<AutoDepositEntity> findAllByMemberAccNum(String memberAccNum);
}
