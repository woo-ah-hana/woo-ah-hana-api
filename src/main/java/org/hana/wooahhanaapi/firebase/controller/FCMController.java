package org.hana.wooahhanaapi.firebase.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;

import org.hana.wooahhanaapi.firebase.port.TestNotificationPort;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FCMController {
    private final TestNotificationPort testNotificationPort;

    @PostMapping("/test")
    public String testNotification(@RequestBody SendNotificationDto sendNotificationDto) throws FirebaseMessagingException {
        return testNotificationPort.testNotification(sendNotificationDto);
    }
}
