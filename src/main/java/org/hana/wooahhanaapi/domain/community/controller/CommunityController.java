package org.hana.wooahhanaapi.domain.community.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.dto.*;
import org.hana.wooahhanaapi.domain.community.dto.RegisterInCommunityRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetMembersResponseDto;
import org.hana.wooahhanaapi.redis.dto.SendValidationCodeReqDto;
import org.hana.wooahhanaapi.domain.community.service.CommunityService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/community")
public class CommunityController {
    private final CommunityService communityService;

    //모임 만들기 버튼
    @PostMapping("/new")
    public String createCommunity(@RequestBody CommunityCreateReqDto dto) {
        this.communityService.createCommunity(dto);
        return "success";
    }

    // 모임 생성시 모임통장 입금자명 확인용 1원 전송
    @PostMapping("/send-code")
    public String validateAccount(@RequestBody SendValidationCodeReqDto dto) {
        this.communityService.sendValidationCode(dto);
        return "success";
    }

    /**
     * 0.모임통장 클릭
     * */
    // 모임통장 거래내역 확인
    @PostMapping("/trsfRecords")
    public List<CommunityTrsfRecordRespDto> getTransferRecords(@RequestBody CommunityTrsfRecordReqDto dto) {
        return this.communityService.getTransferRecord(dto);
    }

    /**
     * 1.모임통장에 입금
     * */
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

    /**
     * 2. 내/모임 계좌관리
     * */
    // 모임 계주 변경
    @PostMapping("/account/changeManager")
    public String changeManager(@RequestBody CommunityChgManagerReqDto dto) {
        this.communityService.changeCommunityManager(dto);
        return "success";
    }

    // 모임통장 회비 금액 / 주기 수정
    @PostMapping("/account/changeFeeInfo")
    public String changeFeeInfo(@RequestBody CommunityChgFeeInfoReqDto dto) {
        this.communityService.changeFeeInfo(dto);
        return "success";
    }

    // 자동이체 설정
    @PostMapping("/account/autoDeposit")
    public String setAutoDeposit(@RequestBody CommunityAutoDepositReqDto dto) {
        this.communityService.setAutoDeposit(dto);
        return "success";
    }

    //자동이체 해제
    @DeleteMapping("/account/autoDeposit")
    public String deleteAutoDeposit(@RequestParam UUID communityId) {
        this.communityService.deleteAutoDeposit(communityId);
        return "success";
    }

    // 개인 계좌 변경
    @PostMapping("/account/changeAccount")
    public String changeMemberAccount(@RequestBody CommunityChgMemAccReqDto dto) {
        this.communityService.changeMemberAccount(dto);
        return "success";
    }

    /**
     * 6. 회비 입금현황
     * */
    // 회비 입금 현황
    @PostMapping("/feeStatus")
    public CommunityFeeStatusRespDto feeStatus(@RequestBody CommunityFeeStatusReqDto dto) {
        return this.communityService.checkFeeStatus(dto);
    }

    //메인화면:모임 목록 가져오기
    @GetMapping("/list")
    public List<CommunitiesResponseDto> getCommunityList() {
        return this.communityService.getCommunities();
    }

    //메인화면:모임 통장 정보 가져오기
    @GetMapping("/info")
    public CommunityInfoResponseDto getCommunityInfo(@RequestParam UUID communityId) {
        return this.communityService.getCommunityInfo(communityId);
    }

    // 모임 전체 정보 반환
    @GetMapping("/{communityId}")
    public CommunityFullInfoResponseDto getCommunityFullInfo(@PathVariable UUID communityId) {
        return this.communityService.getCommunityFullInfo(communityId);
    }
    //모임통장에 속한 멤버 리스트 가져오기
    @GetMapping("/member-list/{communityId}")
    public List<GetMembersResponseDto> getCommunityMembers(@PathVariable UUID communityId) {
        return this.communityService.getMembers(communityId);
    }

    @PostMapping("/register")
    public RegisterInCommunityResponseDto registerInCommunity(@RequestBody RegisterInCommunityRequestDto requestDto){
        return this.communityService.registerInCommunity(requestDto);
    }

    @PostMapping("/quit/{communityId}")
    public String quitFromCommunity(@PathVariable UUID communityId) {
        this.communityService.quitFromCommunity(communityId);
        return "success";
    }

    @PostMapping("/expense-info")
    public GetExpenseInfoRespDto getExpenseInfo(@RequestBody GetExpenseInfoReqDto dto) {
        return this.communityService.getExpenseInfo(dto);
    }
}
