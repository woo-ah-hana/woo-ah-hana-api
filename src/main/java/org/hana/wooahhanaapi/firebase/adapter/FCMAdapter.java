package org.hana.wooahhanaapi.firebase.adapter;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;
import org.hana.wooahhanaapi.firebase.exception.FirebaseException;
import org.hana.wooahhanaapi.firebase.port.TestNotificationPort;
import org.springframework.stereotype.Service;

@Service
public class FCMAdapter implements TestNotificationPort {

    @Override
    public String testNotification(SendNotificationDto dto){
        Notification notification = Notification.builder()
                .setTitle(dto.getTitle())
                .setBody(dto.getBody())
                .build();
        Message message = Message.builder()
                .setToken(dto.getToken())
                .setNotification(notification)
                .build();

        try{
            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            System.out.println(e.getMessage());
            throw new FirebaseException("Firebase Exception");
        }
    }
}
