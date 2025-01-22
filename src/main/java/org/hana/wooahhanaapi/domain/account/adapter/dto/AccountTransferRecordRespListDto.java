package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountTransferRecordRespListDto {

    private String tranDate;
    private String tranTime;
    private String inoutType;
    private String tranType;
    private String printContent;
    private String tranAmt;
    private String afterBalanceAmt;
    private String branchName;

}
