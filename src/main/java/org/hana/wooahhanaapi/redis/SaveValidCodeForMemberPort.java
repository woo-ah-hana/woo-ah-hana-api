package org.hana.wooahhanaapi.redis;

public interface SaveValidCodeForMemberPort {
    void saveValidCodeForMember(String phoneNumber, String validCode);
}
