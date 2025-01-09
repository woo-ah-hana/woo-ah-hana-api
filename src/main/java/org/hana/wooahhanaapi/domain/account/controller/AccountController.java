package org.hana.wooahhanaapi.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    public AccountCreateRespDto createAccount(@RequestBody AccountCreateReqDto accountCreateReqDto) {
        return this.accountService.createAccount(accountCreateReqDto);
    }

    @PostMapping("/record")
    public AccountTransferRecordRespDto recordAccount(@RequestBody AccountTransferRecordReqDto accountTransferRecordReqDto) {
        return this.accountService.getTransferRecord(accountTransferRecordReqDto);
    }
}
