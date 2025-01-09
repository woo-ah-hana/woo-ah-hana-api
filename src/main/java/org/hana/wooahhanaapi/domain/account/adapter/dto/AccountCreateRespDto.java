package org.hana.wooahhanaapi.domain.account.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRespDto implements Serializable {
    @JsonProperty("is_success")
    private Boolean isSuccess;
    private String message;
    private AccountCreateRespDataDto data;
}
