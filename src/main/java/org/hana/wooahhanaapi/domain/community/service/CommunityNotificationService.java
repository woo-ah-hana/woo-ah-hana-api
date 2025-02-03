package org.hana.wooahhanaapi.domain.community.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;
import org.hana.wooahhanaapi.firebase.exception.FirebaseException;
import org.hana.wooahhanaapi.firebase.port.NotifyPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityNotificationService {
    private final MemberRepository memberRepository;
    private final NotifyPort notifyPort;

    public String sendToUnpaidMember(UUID memberId) {
        try{
            MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow();
            SendNotificationDto sendNotificationDto = SendNotificationDto.builder()
                    .title("회비입금 알림")
                    .body(memberEntity.getName()+"님 회비입금 알림이 도착했어요!")
                    .token(memberEntity.getFcmToken())
                    .build();
            return notifyPort.sendNotification(sendNotificationDto);
        }catch (Exception e){
            if(e instanceof UserNotFoundException){
                throw new UserNotFoundException("멤버를 찾을 수 없읍니다.");
            }else {
                System.out.println(e.getMessage());
                throw new FirebaseException("Firebase Exception");
            }
        }
    }
}
