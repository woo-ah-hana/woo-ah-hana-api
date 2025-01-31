package org.hana.wooahhanaapi.domain.account.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccountCreateRespDto implements Serializable {
    private Boolean isSuccess;
    private String message;
    private AccountCreateRespDataDto data;
}
