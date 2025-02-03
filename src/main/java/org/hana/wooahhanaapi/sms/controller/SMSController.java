package org.hana.wooahhanaapi.sms.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.sms.port.SMSSendPort;
import org.hana.wooahhanaapi.sms.port.SMSValidationPort;
import org.hana.wooahhanaapi.redis.dto.MemberValidationConfirmDto;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class SMSController {

    private final SMSValidationPort smsValidationPort;
    private final SMSSendPort smsSendPort;

    // 문자 전송
    @PostMapping("/send-code")
    public boolean sendValidationCode(@RequestParam String phoneNumber) {
        return smsSendPort.sendOne(phoneNumber);
    }

    // 코드 인증
    @PostMapping("/valid-code")
    public boolean validateCode(@RequestBody MemberValidationConfirmDto dto) {
        return smsValidationPort.validateCode(dto);
    }

}
