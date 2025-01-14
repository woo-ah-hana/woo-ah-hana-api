package org.hana.wooahhanaapi.utils.redis;

import org.hana.wooahhanaapi.utils.redis.dto.AccountValidationConfirmDto;

public interface ValidateAccountPort {
    boolean validateAccount(AccountValidationConfirmDto dto);
}
