package org.hana.wooahhanaapi.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.port.AccountCreatePort;
import org.hana.wooahhanaapi.domain.account.port.AccountTransferPort;
import org.hana.wooahhanaapi.domain.account.port.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.port.BankCreatePort;
import org.hana.wooahhanaapi.domain.account.port.GetAccountInfoPort;
import org.hana.wooahhanaapi.domain.account.dto.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountCreatePort accountCreatePort;
    private final BankCreatePort bankCreatePort;
    private final AccountTransferRecordPort accountTransferRecordPort;
    private final GetAccountInfoPort getAccountInfoPort;
    private final AccountTransferPort accountTransferPort;

    public AccountCreateRespDto createAccount(AccountCreateReqDto accountCreateReqDto) {
        return accountCreatePort.createNewAccount(accountCreateReqDto);
    }

    public BankCreateRespDto createBank(BankCreateReqDto bankCreateReqDto) {
        return bankCreatePort.createNewBank(bankCreateReqDto);
    }

    public AccountTransferRecordRespDto getTransferRecord(AccountTransferRecordReqDto accountTransferRecordReqDto) {
        return accountTransferRecordPort.getTransferRecord(accountTransferRecordReqDto);
    }

    public GetAccountInfoRespDto getAccountInfo(GetAccountInfoReqDto getAccountInfoReqDto) {
        return getAccountInfoPort.getAccountInfo(getAccountInfoReqDto);
    }

    public AccountTransferRespDto createTransfer(SimplifiedTransferReqDto simplifiedTransferReqDto) {
        return accountTransferPort.createAccountTransfer(simplifiedTransferReqDto);
    }
}
