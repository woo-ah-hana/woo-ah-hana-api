package org.hana.wooahhanaapi.adapter;

import org.hana.wooahhanaapi.domain.account.adapter.AccountAdapter;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.exception.DuplicateAccountException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class AccountAdapterTest {
    @Autowired
    private AccountAdapter accountAdapter;

    @BeforeEach
    void seed() {

    }

    @Test
    void createNewAccount() {
        // given
        String bankTranId = "001"; //하나은행
        String accountType = "0";
        String accountNumber = "3561417485833";
        String productName = "자유입출금통장";
        AccountCreateReqDto accountCreateReqDto = new AccountCreateReqDto(bankTranId, accountType, accountNumber, productName);

        // when
//        AccountCreateRespDto result = accountAdapter.createNewAccount(accountCreateReqDto);
        // then
        Assertions.assertThrows(DuplicateAccountException.class, () -> accountAdapter.createNewAccount(accountCreateReqDto));
    }
}
