package org.hana.wooahhanaapi.sms.port;

import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.hana.wooahhanaapi.utils.redis.dto.MemberValidationConfirmDto;

public interface SMSValidationPort {
    boolean sendOne(String phoneNumber);
    boolean validateCode(MemberValidationConfirmDto dto);

}
