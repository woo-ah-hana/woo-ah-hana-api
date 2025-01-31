package org.hana.wooahhanaapi.firebase.port;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;

public interface TestNotificationPort {
    String testNotification(SendNotificationDto dto) throws FirebaseMessagingException;
}
