package org.hana.wooahhanaapi.utils.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreateReqDto {
    private String bankTranId;
    private String accountType;
    private String accountNumber;
    private String productName;
}
