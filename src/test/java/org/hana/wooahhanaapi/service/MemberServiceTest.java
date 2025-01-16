package org.hana.wooahhanaapi.service;

import org.hana.wooahhanaapi.domain.member.controller.MemberController;
import org.hana.wooahhanaapi.domain.member.dto.LoginRequestDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.DuplicateUsernameException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberController memberController;

    @BeforeEach
    void seed() {
        MemberEntity memberEntity = MemberEntity.create(
                "010-7767-3813",
                "윤영헌",
                "1234",
                "010-7767-3813",
                "1111111111111",
                "001"
        );
        memberRepository.save(memberEntity);
    }

    @Test
    void signUp() {
        // given
        SignUpRequestDto request = SignUpRequestDto.builder()
                .username("010-2653-0957")
                .password("hj1234!")
                .name("함형주")
                .phoneNumber("01026530957")
                .accountNumber("3561417485843")
                .bankTranId("002")
                .build();
        memberService.signUp(request);
        // when
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
        Assertions.assertThrows(DuplicateUsernameException.class, () -> memberService.signUp(request2));
    }

//@WithMockUser()로 로그인 상태 만드는 법
//    @Test
//    @WithMockUser()
//    void getMemberInfo() {
//        // given
//
//        // when
//        memberService.getMemberInfo();
//        // then
////        Assertions.assertThrows(UserNotLoginException.class, () -> memberService.getMemberInfo());
//    }


}
