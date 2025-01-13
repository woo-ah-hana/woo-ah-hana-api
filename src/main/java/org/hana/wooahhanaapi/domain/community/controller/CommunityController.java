package org.hana.wooahhanaapi.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountValidationReqDto;
import org.hana.wooahhanaapi.domain.community.dto.CommunityChgManagerReqDto;
import org.hana.wooahhanaapi.domain.community.dto.CommunityCreateReqDto;
import org.hana.wooahhanaapi.domain.community.service.CommunityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    @PostMapping("/new")
    public String createCommunity(@RequestBody CommunityCreateReqDto dto) {
        this.communityService.createCommunity(dto);
        return "success";
    }

    // 모임 생성시 모임통장 입금자명 확인용 1원 전송
    @PostMapping("/new/validAccountReq")
    public String validateAccount(@RequestBody AccountValidationReqDto dto) {
        this.communityService.validateAccountRequest(dto);
        return "success";
    }

//    @PostMapping("/new/validAccountConfirm")
//    public boolean validateAccountConfirm(AccountValidationConfirmDto dto) {
//        return this.communityService.validateAccountConfirm(dto);
//    }

    @PostMapping("/changeManager")
    public String changeManager(@RequestBody CommunityChgManagerReqDto dto) {
        this.communityService.changeCommunityManager(dto);
        return "success";
    }

}
