package org.hana.wooahhanaapi.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.AccountCreatePort;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountCreatePort accountCreatePort;
    private final AccountTransferRecordPort accountTransferRecordPort;

    public AccountCreateRespDto createAccount(AccountCreateReqDto accountCreateReqDto) {
        return accountCreatePort.createNewAccount(accountCreateReqDto);
    }

    public AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto accountTransferRecordReqDto) {
        return accountTransferRecordPort.getTransferRecord(accountTransferRecordReqDto);
    }
}
