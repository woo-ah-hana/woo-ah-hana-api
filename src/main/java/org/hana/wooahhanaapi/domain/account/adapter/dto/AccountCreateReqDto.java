package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateReqDto {

    @JsonProperty("bank_tran_id")
    private String bankTranId;
    @JsonProperty("account_type")
    private String accountType;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("product_name")
    private String productName;
}
