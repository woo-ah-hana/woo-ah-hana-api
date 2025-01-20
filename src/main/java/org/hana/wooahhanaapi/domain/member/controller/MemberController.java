package org.hana.wooahhanaapi.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.exception.NotAMemberException;
import org.hana.wooahhanaapi.domain.member.dto.*;
import org.hana.wooahhanaapi.domain.member.exception.PasswordNotMatchException;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.hana.wooahhanaapi.utils.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequestDto signUpRequestDto){
        return this.memberService.signUp(signUpRequestDto);
    }

//    @PostMapping("/signUp/accountAuth")
//    public String signUpAccountAuth(@RequestBody SignUpRequestDto signUpRequestDto){}

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtProvider.createAccessToken(loginRequestDto.getUsername(), authentication.getName());
            String refreshToken = jwtProvider.createRefreshToken(loginRequestDto.getUsername(), authentication.getName());

            return LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
        }catch (BadCredentialsException e){
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
    }

    @GetMapping("/info")
    public MemberResponseDto getMemberInfo(){
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
}
