package org.hana.wooahhanaapi.domain.account.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SimplifiedTransferReqDto {
    private String accountNumber;
    private String bankTranId;
    private String printContent;
    private String inoutType;
    private String tranAmt;
}
