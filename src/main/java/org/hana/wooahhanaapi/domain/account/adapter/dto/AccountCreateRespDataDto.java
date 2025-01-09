package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRespDataDto implements Serializable {

    @JsonProperty("bank_tran_id")
    private String bankTranId;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_type")
    private String accountType;
    @JsonProperty("id")
    private String id;
    @JsonProperty("available_amt")
    private int availableAmt;
    @JsonProperty("product_name")
    private String productName;
}
