package org.hana.wooahhanaapi.domain.community.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.community.adapter.dto.AccountValidationConfirmDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommunityAdapter implements ValidateAccountPort, SaveValidCodePort {
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean validateAccount(AccountValidationConfirmDto dto){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Objects.requireNonNull(valueOperations.get(dto.getAccountNumber())).equals(dto.getValidationCode());
    }

    @Override
    public void saveValidCode(String accountNumber, String validCode){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Long expiredTime = 300L;
        valueOperations.set(accountNumber, validCode, expiredTime, TimeUnit.SECONDS);
    }
}
