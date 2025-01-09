package org.hana.wooahhanaapi.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.AccountPort;
import org.hana.wooahhanaapi.domain.account.adapter.BankCreatePort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.BankCreateRespDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountPort accountPort;
    private final BankCreatePort bankCreatePort;

    public AccountCreateRespDto createAccount(AccountCreateReqDto accountCreateReqDto) {
        return accountPort.createNewAccount(accountCreateReqDto);
    }

    public BankCreateRespDto createBank(BankCreateReqDto bankCreateReqDto) {
        return bankCreatePort.createNewBank(bankCreateReqDto);
    }
}
