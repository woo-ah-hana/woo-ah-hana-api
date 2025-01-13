package org.hana.wooahhanaapi.domain.community.adapter;

import org.hana.wooahhanaapi.domain.community.adapter.dto.AccountValidationConfirmDto;

public interface ValidateAccountPort {
    boolean validateAccount(AccountValidationConfirmDto dto);
}
