package org.hana.wooahhanaapi.firebase.port;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.hana.wooahhanaapi.firebase.dto.SendNotificationDto;

public interface NotifyToUnpaidMemberPort {
    String notifyToUnpaidMember(SendNotificationDto dto) throws FirebaseMessagingException;
}
