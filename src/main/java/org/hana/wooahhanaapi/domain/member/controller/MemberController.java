package org.hana.wooahhanaapi.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.dto.LoginResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.MemberResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.LoginRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.*;
import org.hana.wooahhanaapi.domain.member.service.AuthService;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return this.memberService.signUp(signUpRequestDto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {
        return this.authService.login(loginRequestDto);
    }

    @GetMapping("/logout")
    public String logout(){
        return memberService.logout();
    }

    @GetMapping("/info")
    public MemberInfoResponseDto getMemberInfo(){
        return this.memberService.getMemberInfo();
    }

    @GetMapping("/my-account/info")
    public MyAccountResponseDto getMyAccountInfo(){
        return this.memberService.getMyAccountInfo();
    }

    @GetMapping("/name")
    public String getMemberName(@RequestParam(required = false) UUID id) {
        return this.memberService.getMemberName(id);
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody ChangePasswordReqDto reqDto) {
        return this.memberService.changePassword(reqDto);
    }

    @GetMapping("/inquiry")
    public InquiryMemberRespDto inquiryMember(@RequestParam(required = false) String id) {
        return this.memberService.inquiryMember(id);
    }
}
