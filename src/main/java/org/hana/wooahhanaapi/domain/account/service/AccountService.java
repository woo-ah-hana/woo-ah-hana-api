package org.hana.wooahhanaapi.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.utils.adapter.AccountPort;
import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.utils.adapter.dto.AccountCreateRespDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private AccountPort accountPort;

    public AccountCreateRespDto createAccount(AccountCreateReqDto accountCreateReqDto) {
        return accountPort.createNewAccount(accountCreateReqDto);
    }
}
