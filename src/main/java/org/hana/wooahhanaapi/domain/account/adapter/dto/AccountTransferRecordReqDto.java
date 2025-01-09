package org.hana.wooahhanaapi.domain.account.adapter.dto;

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
    private String fintechUseNum;
    private String inquiryType;
    private String inquiryBase;
    private String fromDate;
    private String fromTime;
    private String toDate;
    private String toTime;
    private String sortOrder;
    private String tranDtime;
    private String beforeInquiryTraceInfo;
}
