package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountTransferRecordRespDataDto {

    private String apiTranId;
    private String resCode;
    private String resMessage;
    private String apiTranDtm;
    private String bankTranId;
    private String bankTranDate;
    private String bankCodeTran;
    private String bankRspCode;
    private String bankRspMessage;
    private String fintechUseNum;
    private String balanceAmt;
    private String pageRecordCnt;
    private String nextPageYn;
    private String beforeInquiryTraceInfo;
    private List<AccountTransferRecordRespListDto> resList;
}
