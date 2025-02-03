package org.hana.wooahhanaapi.utils.redis;

public interface SaveValidCodePort {
    void saveValidCode(String accountNumber, String validCode);
    void saveValidCodeForMember(String phoneNumber, String validCode);
}
