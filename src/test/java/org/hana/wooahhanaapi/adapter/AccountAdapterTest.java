package org.hana.wooahhanaapi.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.AccountAdapter;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.exception.DuplicateAccountException;
import org.hana.wooahhanaapi.domain.account.exception.TransferNotValidException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Random;


@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class AccountAdapterTest {
    @Autowired
    private AccountAdapter accountAdapter;

    @BeforeEach
    void seed() {
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
        //이미 시딩된 account
        String bankTranId = "003"; //하나은행
        String accountType = "0";
        String accountNumber = "2150094621845";
        String productName = "자유입출금통장";
        AccountCreateReqDto duplicatedAccount = new AccountCreateReqDto(bankTranId, accountType, accountNumber, productName);

        //새로운 account
        String newBankTranId ="111";
        Random rand = new Random();
        Long randomNumber = rand.nextLong(9999999999L);
        String newAccountNumber = "123" + String.valueOf(randomNumber);

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
                .accountNumber("111112312312")
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
        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("003") // 하나은행
                .accountNumber("2150094621845")
                .fromDate("2024-01-02")
                .toDate("2025-01-20")
                .build();

        //존재하지 않는 계좌
        AccountTransferRecordReqDto notExistedReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber("8888888888877")
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
        GetAccountInfoReqDto reqDto = new GetAccountInfoReqDto("001","00","2025-01-17","2150094621845");

        //존재하지 않는 계좌
        GetAccountInfoReqDto notExistedReqDto = new GetAccountInfoReqDto("003","00","2025-01-17","8888888888877");

        //when
        GetAccountInfoRespDto result = accountAdapter.getAccountInfo(reqDto);

        //then
        Assertions.assertEquals("성공적으로 계좌를 불러왔습니다.",result.getData().getRspMessage());

        //에러 처리
        Assertions.assertThrows(AccountNotFoundException.class, () -> accountAdapter.getAccountInfo(notExistedReqDto));
    }
}
