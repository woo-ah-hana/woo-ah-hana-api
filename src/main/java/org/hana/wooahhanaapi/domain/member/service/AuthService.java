package org.hana.wooahhanaapi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.dto.LoginRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.LoginResponseDto;
import org.hana.wooahhanaapi.domain.member.exception.PasswordNotMatchException;
import org.hana.wooahhanaapi.utils.security.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
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
            System.out.println(e.getMessage());
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
    }
}
