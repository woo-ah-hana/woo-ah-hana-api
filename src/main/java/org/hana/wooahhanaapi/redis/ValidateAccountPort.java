package org.hana.wooahhanaapi.redis;

import org.hana.wooahhanaapi.redis.dto.AccountValidationConfirmDto;

public interface ValidateAccountPort {
    boolean validateAccount(AccountValidationConfirmDto dto);
}
