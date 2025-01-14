package org.hana.wooahhanaapi.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.dto.*;
import org.hana.wooahhanaapi.utils.redis.dto.SendValidationCodeReqDto;
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
    @PostMapping("/new/send-code")
    public String validateAccount(@RequestBody SendValidationCodeReqDto dto) {
        this.communityService.sendValidationCode(dto);
        return "success";
    }

//    @PostMapping("/new/validAccountConfirm")
//    public boolean validateAccountConfirm(AccountValidationConfirmDto dto) {
//        return this.communityService.validateAccountConfirm(dto);
//    }

    // 모임 계주 변경
    @PostMapping("/changeManager")
    public String changeManager(@RequestBody CommunityChgManagerReqDto dto) {
        this.communityService.changeCommunityManager(dto);
        return "success";
    }

    // 회비 입금 현황
    @PostMapping("/feeStatus")
    public CommunityFeeStatusRespDto feeStatus(@RequestBody CommunityFeeStatusReqDto dto) {
        return this.communityService.checkFeeStatus(dto);
    }

    // 모임통장에 입금 클릭 후 정보 불러오기
    @PostMapping("/deposit/info")
    public CommunityDepositInfoRespDto depositInfo(@RequestBody CommunityDepositInfoReqDto dto) {
        return this.communityService.depositToAccountInfo(dto);
    }

    // 모임통장에 입금 수행
    @PostMapping("/deposit")
    public String deposit(@RequestBody CommunityDepositReqDto dto) {
        this.communityService.depositToAccount(dto);
        return "success";
    }

}
