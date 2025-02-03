package org.hana.wooahhanaapi.sms.port;

public interface SMSSendPort {
    boolean sendOne(String phoneNumber);
}
