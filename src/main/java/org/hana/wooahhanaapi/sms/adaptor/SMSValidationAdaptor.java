package org.hana.wooahhanaapi.sms.adaptor;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.hana.wooahhanaapi.sms.port.SMSValidationPort;
import org.hana.wooahhanaapi.utils.redis.SaveValidCodePort;
import org.hana.wooahhanaapi.utils.redis.ValidateMemberPort;
import org.hana.wooahhanaapi.utils.redis.dto.MemberValidationConfirmDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class SMSValidationAdaptor implements SMSValidationPort {

    private DefaultMessageService messageService;
    private final ValidateMemberPort validateMemberPort;
    private final SaveValidCodePort saveValidCodePort;

    @Value("${COOLSMS_API_KEY}")
    private String apiKey;
    @Value("${COOLSMS_API_SECRET_KEY}")
    private String apiSecretKey;

    @PostConstruct
    public void initialize() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey,
                apiSecretKey,
                "https://api.coolsms.co.kr");
    }

    @Override
    public boolean sendOne(String phoneNumber){
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        String randomNumber = "" + ThreadLocalRandom.current().nextInt(100000, 1000000);
        message.setFrom("01045357321");
        message.setTo(phoneNumber);
        message.setText("[우아하나] 인증번호 : " + randomNumber);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        saveValidCodePort.saveValidCodeForMember(phoneNumber, randomNumber);
        if(response.getStatusCode().equals("2000")) { // 전송 성공
            return true;
        }
        return false;
    }

    @Override
    public boolean validateCode(MemberValidationConfirmDto dto) {
        return validateMemberPort.validateMember(dto);
    }
}
