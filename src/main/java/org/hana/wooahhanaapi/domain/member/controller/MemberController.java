package org.hana.wooahhanaapi.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.exception.NotAMemberException;
import org.hana.wooahhanaapi.domain.member.dto.LoginResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.MemberResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.LoginRequestDto;
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
        }catch (BadCredentialsException e){
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
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
    }

    @GetMapping("/info")
    public MemberResponseDto getMemberInfo(){
        return this.memberService.getMemberInfo();
    }

    @GetMapping("")
    public String getMemberName(@RequestParam(required = false) UUID id) {
        try{
            String name = this.memberService.getMemberName(id);
            return name;
        } catch (Exception e) {
            throw new NotAMemberException("id에 해당하는 member가 없습니다");
        }
    }
}
