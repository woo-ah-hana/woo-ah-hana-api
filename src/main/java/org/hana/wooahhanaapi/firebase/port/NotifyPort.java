package org.hana.wooahhanaapi.firebase.port;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;

public interface NotifyPort {
    String sendNotification(SendNotificationDto dto) throws FirebaseMessagingException;
}
