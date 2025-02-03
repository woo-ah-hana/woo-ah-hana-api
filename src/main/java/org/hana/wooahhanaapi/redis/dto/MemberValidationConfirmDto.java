package org.hana.wooahhanaapi.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberValidationConfirmDto {
    private String phoneNumber;
    private String validationCode;
}
