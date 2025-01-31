package org.hana.wooahhanaapi.domain.firebase.port;

public interface SendNotificationToUnpaidMemberPort {
    String sendNotificationToUnpaidMember(String token, String title, String body);
}
