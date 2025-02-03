package org.hana.wooahhanaapi.sms.port;

import org.hana.wooahhanaapi.redis.dto.MemberValidationConfirmDto;

public interface SMSValidationPort {
    boolean validateCode(MemberValidationConfirmDto dto);
}
