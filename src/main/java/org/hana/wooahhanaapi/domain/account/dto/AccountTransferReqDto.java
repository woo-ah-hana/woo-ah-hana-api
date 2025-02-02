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
public class AccountTransferReqDto {

    private String accountNumber;
    private String tranDate;
    private String tranTime;
    private String inoutType;
    private String tranType;
    private String printContent;
    private String tranAmt;
    private String branchName;
    private String bankTranId;
    @Builder.Default
    private String apiTranDtm = "00";
    @Builder.Default
    private String apiTranId = "00";
    @Builder.Default
    private String bankCodeTran = "00";
    @Builder.Default
    private String bankRspCode = "00";
    @Builder.Default
    private String fintechUseNum = "00";
    @Builder.Default
    private String beforInquiryTraceInfo = "00";

}
