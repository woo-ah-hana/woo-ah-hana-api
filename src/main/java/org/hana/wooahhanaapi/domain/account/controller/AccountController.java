package org.hana.wooahhanaapi.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateRespDto;
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
}
