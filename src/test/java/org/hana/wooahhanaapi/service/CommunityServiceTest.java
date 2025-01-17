package org.hana.wooahhanaapi.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferPort;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespDataDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespListDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.SimplifiedTransferReqDto;
import org.hana.wooahhanaapi.domain.community.dto.CommunityCreateReqDto;
import org.hana.wooahhanaapi.domain.community.service.CommunityService;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.hana.wooahhanaapi.utils.redis.SaveValidCodePort;
import org.hana.wooahhanaapi.utils.redis.dto.AccountValidationConfirmDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public class CommunityServiceTest {
    @Autowired
    private MemberService memberService;
    @Autowired
    private AccountTransferPort accountTransferPort;
    @Autowired
    private SaveValidCodePort saveValidCodePort;
    @Autowired
    private AccountTransferRecordPort accountTransferRecordPort;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    public void setUp() {
        MemberEntity m1 = MemberEntity.create(
                "01026530957", "hj1234!", "함형주", "01026530957", "3561057204496", "002");
        MemberEntity m2 = MemberEntity.create(
                "01012345678", "sj1234!", "최선정", "01012345678", "2150094621845", "003");
//        SignUpRequestDto mreq1 = SignUpRequestDto.builder()
//                .username("01026530957")
//                .password("hj1234!")
//                .name("함형주")
//                .phoneNumber("01026530957")
//                .accountNumber("3561057204496")
//                .bankTranId("002")
//                .build();
//        SignUpRequestDto mreq2 = SignUpRequestDto.builder()
//                .username("01012345678")
//                .password("sj1234!")
//                .name("최선정")
//                .phoneNumber("01012345678")
//                .accountNumber("2150094621845")
//                .bankTranId("003")
//                .build();
//        SignUpRequestDto mreq3 = SignUpRequestDto.builder()
//                .username("01039388377")
//                .password("yh1234!")
//                .name("윤영헌")
//                .phoneNumber("01039388377")
//                .accountNumber("3561024215509")
//                .bankTranId("003")
//                .build();

//        memberService.signUp(mreq1);
//        memberService.signUp(mreq2);
//        memberService.signUp(mreq3);
        // 함형주, 최선정, 윤영헌 3명 회원 가입된 상태
        memberRepository.save(m1);
    }

    @Test
    public void sendValidationCode() {
        // given
        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
        SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("3561057204496")
                .bankTranId("002")
                .inoutType("입금")
                .printContent(validCode)
                .tranAmt("1")
                .build();
        boolean trueOrFalse = false;

        LocalDateTime today = LocalDateTime.now();
        // 날짜 포맷 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fToday = today.format(formatter);

        // when
        accountTransferPort.createAccountTransfer(reqDto); // 입금자명과 함께 1원 전송

        // then
        AccountTransferRecordReqDto recordReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("002")
                .accountNumber("3561057204496")
                .fromDate(fToday)
                .toDate(fToday)
                .build(); // 당일 거래내역 조회
        List<AccountTransferRecordRespListDto> respDto =
                accountTransferRecordPort.getTransferRecord(recordReqDto).getData().getResList();

        for(AccountTransferRecordRespListDto dto : respDto) {
            if(dto.getPrintContent().equals(validCode)) {
                trueOrFalse = true;
            }
        }

        Assertions.assertThat(trueOrFalse).isTrue();
    }

    @Test
    @WithMockUser(username = "01026530957") // 함형주로 로그인한 상태
    public void createCommunity() {

        // given
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        // given
        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
        SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                .accountNumber("3561057204496")
                .bankTranId("002")
                .inoutType("입금")
                .printContent(validCode)
                .tranAmt("1")
                .build();
        boolean trueOrFalse = false;

        LocalDateTime today = LocalDateTime.now();
        // 날짜 포맷 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fToday = today.format(formatter);


    }

}
