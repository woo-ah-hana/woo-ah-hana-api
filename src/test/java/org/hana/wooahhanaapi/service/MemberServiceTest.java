package org.hana.wooahhanaapi.service;

import org.hana.wooahhanaapi.account.exception.MemberNotPresentException;
import org.hana.wooahhanaapi.domain.member.dto.*;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.*;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.member.service.AuthService;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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
    private AuthService authService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeAll
    public void setUp() {
        MemberEntity m1 = MemberEntity.create(
                "01026530956","함형주", passwordEncoder.encode("hj12345!"), "01026530956", "3561057204496", "002","mock_fcm_token");
        memberRepository.save(m1);
    }

    @Test
    void signUp() {

        SignUpRequestDto request = SignUpRequestDto.builder()
                .username("01026530999")
                .password("hj12345!")
                .name("함형주")
                .phoneNumber("01026530999")
                .accountNumber("3561417485843")
                .bankTranId("002")
                .build();

        SignUpRequestDto duplicatedRequest = SignUpRequestDto.builder()
                .username("01026530999")
                .password("hj12345!")
                .name("함형주")
                .phoneNumber("01026530999")
                .accountNumber("3561417485843")
                .bankTranId("002")
                .build();

        Assertions.assertEquals("01026530999",memberService.signUp(request));

        Assertions.assertThrows(DuplicateUsernameException.class, () -> memberService.signUp(duplicatedRequest));
    }

    @Test
    void getMemberName() {
        MemberEntity hj = memberRepository.findByUsername("01026530956").orElseThrow();

        String result = memberService.getMemberName(hj.getId());

        Assertions.assertEquals("함형주",result);

        UUID uuid = UUID.randomUUID();
        Assertions.assertThrows(MemberNotPresentException.class, () -> memberService.getMemberName(uuid));
    }

    @Test
    void logout() {
        LoginRequestDto requestDto = new LoginRequestDto("01026530956","hj12345!");
        login(requestDto);

        String result = memberService.logout();

        Assertions.assertEquals("로그아웃 성공", result);

        Assertions.assertThrows(UserNotLoginException.class, () -> memberService.logout());
    }

    @Test
    void getMemberInfo() {
        LoginRequestDto requestDto = new LoginRequestDto("01026530956","hj12345!");
        login(requestDto);

        MemberInfoResponseDto result = memberService.getMemberInfo();

        Assertions.assertEquals("01026530956", result.getUsername());
    }

    @Test
    @DisplayName("비밀번호 변경")
    public void changePasswordTest() {

        // given
        MemberEntity hj = memberRepository.findByUsername("01026530956") // 함형주
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(hj, null, hj.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context); // 함형주로 로그인

        // 조건 만족하는 비밀번호
        ChangePasswordReqDto reqDto1 = ChangePasswordReqDto.builder()
                .newPassword("hj1234567!")
                .build();
        // 조건 만족하지 못하는 비밀번호
        ChangePasswordReqDto reqDto2 = ChangePasswordReqDto.builder()
                .newPassword("hj234!")
                .build();

        // case 1. 조건 만족시
        // when
        memberService.changePassword(reqDto1);

        // then
        LoginRequestDto loginRequest = LoginRequestDto.builder()
                .username("01026530956")
                .password("hj1234567!")
                .build();
        // 비밀번호 불일치란 예외가 발생하지 않아야 통과
        assertDoesNotThrow(() -> authService.login(loginRequest));

        // case 2. 조건 불만족시
        // when - then
        LoginRequestDto loginRequest2 = LoginRequestDto.builder()
                .username("01026530956")
                .password("hj234!")
                .build();
        // 비밀번호 조건 불만족 예외가 발생하야 통과
        assertThrows(PasswordNotSatisfyCondException.class, () -> memberService.changePassword(reqDto2));
    }

    @Test
    void loginTest() {
        LoginRequestDto generalLoginRequest = LoginRequestDto.builder().username("01026530956").password("hj12345!").build();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        generalLoginRequest.getUsername(),
                        generalLoginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Assertions.assertEquals(authentication,SecurityContextHolder.getContext().getAuthentication());

        LoginRequestDto badUsernameLoginRequest = LoginRequestDto.builder().username("01026530956").password("hj12345?").build();

        assertThrows(BadCredentialsException.class, ()-> {
            Authentication badAuthentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            badUsernameLoginRequest.getUsername(),
                            badUsernameLoginRequest.getPassword()
                    )
            );
        });
    }

    void login(LoginRequestDto request) {
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
