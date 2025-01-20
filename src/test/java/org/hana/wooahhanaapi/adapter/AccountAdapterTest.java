package org.hana.wooahhanaapi.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.AccountAdapter;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.exception.DuplicateAccountException;
import org.hana.wooahhanaapi.domain.account.exception.TransferNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class AccountAdapterTest {
    @Autowired
    private AccountAdapter accountAdapter;

    @BeforeEach
    public void seed() {
        try{
            String bankTranId = "001"; //하나은행
            String accountType = "0";
            String accountNumber = "1111234123456";
            String productName = "자유입출금통장";
            AccountCreateReqDto account = new AccountCreateReqDto(bankTranId, accountType, accountNumber, productName);
            accountAdapter.createNewAccount(account);
        }catch(DuplicateAccountException e){
            System.out.println("시딩");
        }
    }

    @Test
    void createNewAccount() {
        // given
        //새로운 account
        String newBankTranId ="111";
        Random rand = new Random();
        Long randomNumber = rand.nextLong(9999999999L);
        String newAccountNumber = "123" + String.valueOf(randomNumber);

        //이미 시딩된 account
        String bankTranId = "001"; //하나은행
        String accountType = "0";
        String accountNumber = "1111234123456";
        String productName = "자유입출금통장";
        AccountCreateReqDto duplicatedAccount = new AccountCreateReqDto(bankTranId, accountType, accountNumber, productName);

        AccountCreateReqDto newAccount = new AccountCreateReqDto(newBankTranId, accountType, newAccountNumber, productName);

        // when
        Boolean createNewAccountResult =accountAdapter.createNewAccount(newAccount).getIsSuccess();

        // then
        Assertions.assertTrue(createNewAccountResult);

        //에러처리
        Assertions.assertThrows(DuplicateAccountException.class, () -> accountAdapter.createNewAccount(duplicatedAccount));
    }

    @Test
    void createAccountTransfer() {
        //given
        SimplifiedTransferReqDto depositReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000") //30만원 입금
                .inoutType("입금")
                .printContent("디지털 하나로 훈련 지원금")
                .build();

        SimplifiedTransferReqDto withdrawalReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000") //30만원 출금
                .inoutType("출금")
                .printContent("밥플러스 1호점")
                .build();

        //없는 계좌
        SimplifiedTransferReqDto notExistedReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("0000000000000")
                .bankTranId("001")
                .tranAmt("300000") //30만원 출금
                .inoutType("입금")
                .printContent("밥플러스 1호점")
                .build();

        //when
        AccountTransferRespDto response1 = accountAdapter.createAccountTransfer(depositReqDto);
        AccountTransferRespDto response2 = accountAdapter.createAccountTransfer(withdrawalReqDto);

        //then
        Assertions.assertEquals(300000L,response1.getData());
        Assertions.assertEquals(0,response2.getData());

        //예외 처리
        Assertions.assertThrows(TransferNotValidException.class, () -> accountAdapter.createAccountTransfer(notExistedReqDto));
    }

    @Test
    void getTransferRecord() {
        //given
        SimplifiedTransferReqDto depositReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000") //30만원 입금
                .inoutType("입금")
                .printContent("디지털 하나로 훈련 지원금")
                .build();

        SimplifiedTransferReqDto withdrawalReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000") //30만원 출금
                .inoutType("출금")
                .printContent("밥플러스 1호점")
                .build();
        AccountTransferRespDto response1 = accountAdapter.createAccountTransfer(depositReqDto);
        AccountTransferRespDto response2 = accountAdapter.createAccountTransfer(withdrawalReqDto);

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 날짜를 원하는 형식의 String으로 변환
        String formattedDate = currentDate.format(formatter1);

        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber("1111234123456")
                .fromDate("2024-05-02")
                .toDate(formattedDate)
                .build();

        //존재하지 않는 계좌
        AccountTransferRecordReqDto notExistedReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber("0000000000000")
                .fromDate("2024-01-02")
                .toDate("2025-01-20")
                .build();

        //when
        AccountTransferRecordRespDto result = accountAdapter.getTransferRecord(reqDto);

        //then
        Assertions.assertEquals("성공적으로 거래내역을 불러왔습니다.",result.getData().getResMessage());

        //에러 처리
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountAdapter.getTransferRecord(notExistedReqDto));
    }

    @Test
    void getAccountInfo() {
        //given
        GetAccountInfoReqDto reqDto = new GetAccountInfoReqDto("001","00","2025-01-17","1111234123456");

        //존재하지 않는 계좌
        GetAccountInfoReqDto notExistedReqDto = new GetAccountInfoReqDto("003","00","2025-01-17","0000000000000");

        //when
        GetAccountInfoRespDto result = accountAdapter.getAccountInfo(reqDto);

        //then
        Assertions.assertEquals("성공적으로 계좌를 불러왔습니다.",result.getData().getRspMessage());

        //에러 처리
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountAdapter.getAccountInfo(notExistedReqDto));
    }
}
