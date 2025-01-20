package org.hana.wooahhanaapi.service;

import org.hana.wooahhanaapi.domain.account.exception.MemberNotPresentException;
import org.hana.wooahhanaapi.domain.member.dto.LoginRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.MemberResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.DuplicateUsernameException;
import org.hana.wooahhanaapi.domain.member.exception.PasswordNotMatchException;
import org.hana.wooahhanaapi.domain.member.exception.UserNotLoginException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

@TestPropertySource(locations = "classpath:application-test.yml")
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public void setUp() {
        MemberEntity m1 = MemberEntity.create(
                "01026530956","함형주", passwordEncoder.encode("hj1234!"), "01026530956", "3561057204496", "002");
        memberRepository.save(m1);
    }

    @Test
    void signUp() {
        // given
        //정상 아이디
        SignUpRequestDto request = SignUpRequestDto.builder()
                .username("010-2653-0957")
                .password("hj1234!")
                .name("함형주")
                .phoneNumber("01026530957")
                .accountNumber("3561417485843")
                .bankTranId("002")
                .build();
        //아이디 중복
        SignUpRequestDto request2 = SignUpRequestDto.builder()
                .username("010-2653-0957")
                .password("hj1234!")
                .name("함형주")
                .phoneNumber("01026530957")
                .accountNumber("3561417485843")
                .bankTranId("002")
                .build();
        // then
        Assertions.assertEquals("010-2653-0957",memberService.signUp(request));
        //에러 처리
        Assertions.assertThrows(DuplicateUsernameException.class, () -> memberService.signUp(request2));
    }

    @Test
    void getMemberName() {
        // given
        MemberEntity hj = memberRepository.findByUsername("01026530956").orElseThrow();
        // when
        String result = memberService.getMemberName(hj.getId());
        System.out.println(result);
        // then
        Assertions.assertEquals("함형주",result);
        //에러 처리
        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(MemberNotPresentException.class, () -> memberService.getMemberName(uuid));
    }

    @Test
    void logout() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("01026530956","hj1234!");
        login(requestDto);
        // when
        String result = memberService.logout();
        // then
        Assertions.assertEquals("로그아웃 성공", result);
        //에러처리
        Assertions.assertThrows(UserNotLoginException.class, () -> memberService.logout());
    }

    @Test
    void getMemberInfo() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("01026530956","hj1234!");
        login(requestDto);
        // when
        MemberResponseDto result = memberService.getMemberInfo();
        // then
        Assertions.assertEquals("01026530956", result.getUsername());
    }

    void login(LoginRequestDto request) {
        //when
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            throw new PasswordNotMatchException("비밀번호가 일치하지 않습니다.");
        }
    }
}
