package org.hana.wooahhanaapi.utils.adapter.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRespDataDto implements Serializable {
    private String bankTranId;
    private String accountNumber;
    private String accountType;
    private String id;
    private int availableAmt;
    private String productName;
}
