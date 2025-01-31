package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.firebase.port.NotifyPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityNotificationService {
    private final MemberRepository memberRepository;
    private final NotifyPort notifyPort;

    public String sendToUnpaidMember(UUID memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId).orElseThrow();
        return "";
    }
}
