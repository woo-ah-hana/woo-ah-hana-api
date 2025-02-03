package org.hana.wooahhanaapi.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.redis.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.redis.dto.MemberValidationConfirmDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisAdapter implements ValidateAccountPort, SaveValidCodePort, ValidateMemberPort, SaveValidCodeForMemberPort{
    private final RedisTemplate<String, String> redisTemplate;

    // 계좌 입금자명 인증
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

    // 회원가입 문자인증
    @Override
    public boolean validateMember(MemberValidationConfirmDto dto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return Objects.requireNonNull(valueOperations.get(dto.getPhoneNumber())).equals(dto.getValidationCode());
    }

    @Override
    public void saveValidCodeForMember(String phoneNumber, String validCode) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Long expiredTime = 300L;
        valueOperations.set(phoneNumber, validCode, expiredTime, TimeUnit.SECONDS);
    }
}
