package org.hana.wooahhanaapi.utils.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class AccountCreateRespDataDto {
    private String bankCodeTran;
    private String id;
    private String bankName;
    private String bankTranId;
}
