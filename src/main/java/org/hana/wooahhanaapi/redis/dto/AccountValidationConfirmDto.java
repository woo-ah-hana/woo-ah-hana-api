package org.hana.wooahhanaapi.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountValidationConfirmDto {
    private String accountNumber;
    private String validationCode;
}
