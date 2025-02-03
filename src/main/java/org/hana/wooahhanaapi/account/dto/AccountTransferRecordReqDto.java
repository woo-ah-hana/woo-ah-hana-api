package org.hana.wooahhanaapi.account.dto;

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
public class AccountTransferRecordReqDto {

    private String bankTranId;
    private String accountNumber;
    @Builder.Default
    private String fintechUseNum = "fintech_use_num";
    @Builder.Default
    private String inquiryType = "00";
    @Builder.Default
    private String inquiryBase = "00";
    private String fromDate;
    @Builder.Default
    private String fromTime = "00:00";
    private String toDate;
    @Builder.Default
    private String toTime = "23:59";
    @Builder.Default
    private String sortOrder = "asc";
    @Builder.Default
    private String tranDtime = "00";
    @Builder.Default
    private String beforeInquiryTraceInfo = "00";
}
