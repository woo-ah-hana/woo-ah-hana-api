package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreateRespDataDto implements Serializable {

    private String bankTranId;
    private String accountNumber;
    private String accountType;
    private String id;
    private int availableAmt;
    private String productName;
}
