package org.hana.wooahhanaapi.domain.community.mapper;

import org.hana.wooahhanaapi.domain.community.domain.AutoDeposit;
import org.hana.wooahhanaapi.domain.community.entity.AutoDepositEntity;

public class AutoDepositMapper {

    public static AutoDepositEntity mapDomainToEntity(AutoDeposit autoDeposit) {
        return AutoDepositEntity.builder()
                .memberAccNum(autoDeposit.getMemberAccNum())
                .communityAccNum(autoDeposit.getCommunityAccNum())
                .fee(autoDeposit.getFee())
                .depositDay(autoDeposit.getDepositDay())
                .memberBankTranId(autoDeposit.getMemberBankTranId())
                .build();
    }

    public static AutoDeposit mapEntityToDomain(AutoDepositEntity autoDepositEntity) {
        return AutoDeposit.create(
                autoDepositEntity.getId(), autoDepositEntity.getMemberBankTranId(), autoDepositEntity.getMemberAccNum(), autoDepositEntity.getCommunityAccNum()
                ,autoDepositEntity.getFee(), autoDepositEntity.getDepositDay());
    }
}
