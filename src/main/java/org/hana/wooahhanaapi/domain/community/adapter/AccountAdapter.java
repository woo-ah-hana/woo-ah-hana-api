package org.hana.wooahhanaapi.domain.community.adapter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferReqDto;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.community.adapter.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.domain.community.adapter.dto.SendValidationCodeReqDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountAdapter implements ValidateAccountPort, SendValidCodePort {
    private final RedisTemplate<String, String> redisTemplate;
    private final AccountService accountService;

    @Override
    public boolean validateAccount(AccountValidationConfirmDto dto){
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        System.out.println("cc");
        System.out.println(dto.getAccountNumber());
        System.out.println(valueOperations.get(dto.getAccountNumber()));
        return Objects.requireNonNull(valueOperations.get(dto.getAccountNumber())).equals(dto.getValidationCode());
    }

    @Override
    public void sendValidCode(SendValidationCodeReqDto reqDto){
        System.out.println("aaaaaaaaaa");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
        Long expiredTime = 300L;
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        try{
            // 날짜를 원하는 형식의 String으로 변환
            String formattedDate = currentDate.format(formatter1);
            String formattedTime = currentDate.format(formatter2);
            AccountTransferReqDto dto2 = AccountTransferReqDto.builder()
                    .accountNumber(reqDto.getAccountNumber())
                    .bankTranId(reqDto.getBankTranId())//하나은행:001, 우리은행:002
                    .printContent(validCode)
                    .tranDate(formattedDate)
                    .tranTime(formattedTime)
                    .inoutType("입금")
                    .tranType("결재")
                    .tranAmt("1")
                    .branchName("우아하나")
                    .build();
            System.out.println(dto2.getAccountNumber());
            System.out.println("bbbbbbb");

            accountService.createTransfer(dto2);
            System.out.println("ccccccc");
            valueOperations.set(reqDto.getAccountNumber(), validCode, expiredTime, TimeUnit.SECONDS);
            System.out.println("ddddddddd");
        }catch (Exception e){
            throw new AccountNotFoundException("계좌를 찾을 수 없음");
        }
    }
}
