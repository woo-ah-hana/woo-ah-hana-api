package org.hana.wooahhanaapi.utils.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRespDto {
    private Boolean isSuccess;
    private String message;
    private AccountCreateRespDataDto data;
}
