package org.hana.wooahhanaapi.domain.account.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetAccountInfoRespDataDto {
    private String apiTranId;
    private String resCode;
    private String rspMessage;
    private String apiTranDtm;
    private String bankTranId;
    private String bankTranDate;
    private String bankCodeTran;
    private String bankRspCode;
    private String bankRspMessage;
    private String fintechUseNum;
    private Long balanceAmt;
    private Long availableAmt;
    private String accountType;
    private String productName;
}