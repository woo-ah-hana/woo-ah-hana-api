package org.hana.wooahhanaapi.adapter;

import org.hana.wooahhanaapi.account.adapter.AccountAdapter;
import org.hana.wooahhanaapi.account.dto.*;
import org.hana.wooahhanaapi.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.account.exception.DuplicateAccountException;
import org.hana.wooahhanaapi.account.exception.TransferNotValidException;
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

        String newBankTranId ="111";
        Random rand = new Random();
        Long randomNumber = rand.nextLong(9999999999L);
        String newAccountNumber = "123" + String.valueOf(randomNumber);


        String duplicatedBankTranId = "001"; //하나은행
        String duplicatedAccountNumber = "1111234123456";
        AccountCreateReqDto duplicatedAccount = new AccountCreateReqDto(duplicatedBankTranId, "0", duplicatedAccountNumber, "자유입출금통장");
        AccountCreateReqDto newAccount = new AccountCreateReqDto(newBankTranId, "0", newAccountNumber, "자유입출금통장");

        Boolean createNewAccountResult =accountAdapter.createNewAccount(newAccount).getIsSuccess();

        Assertions.assertTrue(createNewAccountResult);

        Assertions.assertThrows(DuplicateAccountException.class, () -> accountAdapter.createNewAccount(duplicatedAccount));
    }

    @Test
    void createAccountTransfer() {
        SimplifiedTransferReqDto depositReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000")
                .inoutType("입금")
                .printContent("디지털 하나로 훈련 지원금")
                .build();

        SimplifiedTransferReqDto withdrawalReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("1111234123456")
                .bankTranId("001")
                .tranAmt("300000")
                .inoutType("출금")
                .printContent("밥플러스 1호점")
                .build();

        SimplifiedTransferReqDto notExistedReqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("0000000000000")
                .bankTranId("001")
                .tranAmt("300000") //30만원 출금
                .inoutType("입금")
                .printContent("밥플러스 1호점")
                .build();

        AccountTransferRespDto response1 = accountAdapter.createAccountTransfer(depositReqDto);
        AccountTransferRespDto response2 = accountAdapter.createAccountTransfer(withdrawalReqDto);

        Assertions.assertEquals(300000L,response1.getData());
        Assertions.assertEquals(0,response2.getData());

        Assertions.assertThrows(TransferNotValidException.class, () -> accountAdapter.createAccountTransfer(notExistedReqDto));
    }

    @Test
    void getTransferRecord() {
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
                .tranAmt("300000")
                .inoutType("출금")
                .printContent("밥플러스 1호점")
                .build();
        accountAdapter.createAccountTransfer(depositReqDto);
        accountAdapter.createAccountTransfer(withdrawalReqDto);

        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter1);

        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber("1111234123456")
                .fromDate("2024-05-02")
                .toDate(formattedDate)
                .build();

        AccountTransferRecordReqDto notExistedReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001") // 하나은행
                .accountNumber("0000000000000")
                .fromDate("2024-01-02")
                .toDate("2025-01-20")
                .build();

        AccountTransferRecordRespDto result = accountAdapter.getTransferRecord(reqDto);

        Assertions.assertEquals("성공적으로 거래내역을 불러왔습니다.",result.getData().getResMessage());

        Assertions.assertThrows(AccountNotFoundException.class, () -> accountAdapter.getTransferRecord(notExistedReqDto));
    }

    @Test
    void getAccountInfo() {

        GetAccountInfoReqDto reqDto = new GetAccountInfoReqDto("001","00","2025-01-17","1111234123456");

        GetAccountInfoReqDto notExistedReqDto = new GetAccountInfoReqDto("003","00","2025-01-17","0000000000000");

        GetAccountInfoRespDto result = accountAdapter.getAccountInfo(reqDto);

        Assertions.assertEquals("성공적으로 계좌를 불러왔습니다.",result.getData().getRspMessage());

        Assertions.assertThrows(AccountNotFoundException.class, () -> accountAdapter.getAccountInfo(notExistedReqDto));
    }
}
