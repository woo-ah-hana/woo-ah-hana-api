package org.hana.wooahhanaapi.firebase.port;

public interface SendNotificationToUnpaidMemberPort {
    String sendNotificationToUnpaidMember(String token, String title, String body);
}
