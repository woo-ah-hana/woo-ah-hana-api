package org.hana.wooahhanaapi.domain.community.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.service.CommunityNotificationService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community-notification")
public class CommunityNotificationController {
    private final CommunityNotificationService communityNotificationService;

    @PostMapping("/unpaid")
    public String sendToUnpaid(@RequestBody UUID memberId) {
        return communityNotificationService.sendToUnpaidMember(memberId);
    }
}
