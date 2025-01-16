package org.hana.wooahhanaapi.domain.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutoDeposit { // 자동이체 정보 저장

    private UUID id;
    private String memberBankTranId;
    private String memberAccNum;
    private String communityAccNum;
    private String fee;
    private int depositDay;

    public static AutoDeposit create(UUID id, String memberBankTranId, String memberAccNum, String communityAccNum, String fee, int depositDay) {
        return new AutoDeposit(id, memberBankTranId, memberAccNum, communityAccNum, fee, depositDay);
    }

}
