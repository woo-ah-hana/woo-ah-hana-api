package org.hana.wooahhanaapi.sms.port;

import org.hana.wooahhanaapi.redis.dto.MemberValidationConfirmDto;

public interface SMSValidationPort {
    boolean sendOne(String phoneNumber);
    boolean validateCode(MemberValidationConfirmDto dto);

}
