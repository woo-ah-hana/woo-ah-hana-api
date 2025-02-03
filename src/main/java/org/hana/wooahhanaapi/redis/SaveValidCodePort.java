package org.hana.wooahhanaapi.redis;

public interface SaveValidCodePort {
    void saveValidCode(String accountNumber, String validCode);
}
