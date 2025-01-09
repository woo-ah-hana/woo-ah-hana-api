package org.hana.wooahhanaapi.utils.adapter.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRespDto implements Serializable {
    private Boolean isSuccess;
    private String message;
    private AccountCreateRespDataDto data;
}
