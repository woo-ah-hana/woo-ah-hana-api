package org.hana.wooahhanaapi.service;

import org.assertj.core.api.Assertions;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferPort;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferRecordRespListDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.SimplifiedTransferReqDto;
import org.hana.wooahhanaapi.domain.account.exception.IncorrectValidationCodeException;
import org.hana.wooahhanaapi.domain.community.domain.Community;
import org.hana.wooahhanaapi.domain.community.dto.*;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.exception.NoAuthorityException;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.community.service.CommunityService;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.member.service.MemberService;
import org.hana.wooahhanaapi.utils.redis.SaveValidCodePort;
import org.hana.wooahhanaapi.utils.redis.ValidateAccountPort;
import org.hana.wooahhanaapi.utils.redis.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.utils.redis.dto.SendValidationCodeReqDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @Autowired
    private ValidateAccountPort validateAccountPort;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private CommunityService communityService;
    @Autowired
    private MembershipRepository membershipRepository;

    @BeforeAll
    public void setUp() {
        MemberEntity m1 = MemberEntity.create(
                "01026530957", "함형주", "hj1234!","01026530957", "3561057204496", "002");
        MemberEntity m2 = MemberEntity.create(
                "01012345678", "최선정","sj1234!","01012345678", "2150094621845", "003");
        MemberEntity m3 = MemberEntity.create(
                "01039388377", "윤영헌", "yh1234!", "01039388377","3561024215509", "002");
        // 함형주, 최선정, 윤영헌 3명 회원 가입된 상태
        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        CommunityEntity community = CommunityEntity.create(
                memberRepository.findByUsername("01026530957").get().getId(), "모임1",
                "1468299555144", 10L, 20000L, 5L
        );
        // 계주 : 함형주, 이름 : "모임1", 계좌번호 : 1468299555144인 모임 하나 개설
        communityRepository.save(community);

        MembershipEntity ms1 = MembershipEntity.create(m1, community);
        MembershipEntity ms2 = MembershipEntity.create(m2, community);
        MembershipEntity ms3 = MembershipEntity.create(m3, community);
        membershipRepository.save(ms1);
        membershipRepository.save(ms2);
        membershipRepository.save(ms3); // 위 3명의 멤버를 모임1에 가입시킴
    }

//    @Test
//    @DisplayName("인증용 1원 전송")
//    @Transactional
//    public void sendValidationCode() {
//        // given
//        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
//        SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
//                .accountNumber("3561057204496")
//                .bankTranId("002")
//                .inoutType("입금")
//                .printContent(validCode)
//                .tranAmt("1")
//                .build();
//        boolean trueOrFalse = false;
//
//        LocalDateTime today = LocalDateTime.now();
//        // 날짜 포맷 변환
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String fToday = today.format(formatter);
//
//        // when
//        accountTransferPort.createAccountTransfer(reqDto); // 입금자명과 함께 1원 전송
//
//        // then
//        AccountTransferRecordReqDto recordReqDto = AccountTransferRecordReqDto.builder()
//                .bankTranId("002")
//                .accountNumber("3561057204496")
//                .fromDate(fToday)
//                .toDate(fToday)
//                .build(); // 당일 거래내역 조회
//        List<AccountTransferRecordRespListDto> respDto =
//                accountTransferRecordPort.getTransferRecord(recordReqDto).getData().getResList();
//
//        for(AccountTransferRecordRespListDto dto : respDto) {
//            if(dto.getPrintContent().equals(validCode)) {
//                trueOrFalse = true;
//            }
//        }
//
//        Assertions.assertThat(trueOrFalse).isTrue();
//    }
//
//    @Test
//    @DisplayName("모임 생성")
//    @Transactional
//    public void createCommunity() {
//
//        // given
//        // 현재 로그인한 사용자 정보 가져오기
//        MemberEntity loggedInUser = memberRepository.findByUsername("01026530957")
//                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
//
//        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
//        SendValidationCodeReqDto codeReqDto = SendValidationCodeReqDto.builder()
//                .bankTranId("001")
//                        .accountNumber("1468214722317")
//                                .build(); // 모임 개설 위한 다른 계좌
//
//        // when
//        saveValidCodePort.saveValidCode(codeReqDto.getAccountNumber(),validCode);
//        AccountValidationConfirmDto accValidDto = AccountValidationConfirmDto.builder()
//                .accountNumber("1468214722317")
//                .validationCode(validCode)
//                .build();
//
//        // then
//        assertDoesNotThrow(() -> validateAccountPort.validateAccount(accValidDto));
//        CommunityEntity newCommunity = CommunityEntity.create(
//                loggedInUser.getId(),
//                "안녕하세요",
//                "1468214722317",
//                10L,
//                40000L,
//                10L);
//
//        communityRepository.save(newCommunity);
//        CommunityEntity foundCommunity = communityRepository.findByAccountNumber("1468214722317")
//                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));
//        Assertions.assertThat(foundCommunity.getId()).isEqualTo(newCommunity.getId());
//        Assertions.assertThat(foundCommunity.getName()).isEqualTo(newCommunity.getName());
//        Assertions.assertThat(foundCommunity.getAccountNumber()).isEqualTo(newCommunity.getAccountNumber());
//
//    }

    @Test
    @DisplayName("모임의 계주 변경")
    @Transactional
    public void changeManager() {

        // given
        CommunityEntity foundCommunity = communityRepository.findByAccountNumber("1468299555144")
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다.")); // 모임1

        MemberEntity nextManager = memberRepository.findByUsername("01012345678")
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다.")); // 최선정

        CommunityChgManagerReqDto reqDto = CommunityChgManagerReqDto.builder()
                .communityId(foundCommunity.getId())
                .memberId(nextManager.getId()) // 최선정으로 계주 변경하려함
                .build();

        // when
        communityService.changeCommunityManager(reqDto);

        // then
        CommunityEntity updatedCommunity = communityRepository.findByAccountNumber("1468299555144")
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        Assertions.assertThat(updatedCommunity.getManagerId()).isEqualTo(nextManager.getId());

    }

//    @Test
//    @DisplayName("회비 납입 여부 체크")
//    @Transactional
//    public void checkFeeStatus() {
//        // given
//        MemberEntity hj = memberRepository.findByUsername("01026530957")
//                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
//        MemberEntity sj = memberRepository.findByUsername("01012345678")
//                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
//        SimplifiedTransferReqDto reqDto1 = SimplifiedTransferReqDto.builder()
//                .accountNumber("1468299555144")
//                .bankTranId("001")
//                .inoutType("입금")
//                .printContent(hj.getName())
//                .tranAmt("30000") // 함형주는 납부
//                .build();
//        SimplifiedTransferReqDto reqDto2 = SimplifiedTransferReqDto.builder()
//                .accountNumber("1468299555144")
//                .bankTranId("001")
//                .inoutType("입금")
//                .printContent(sj.getName())
//                .tranAmt("10000") // 최선정은 미납(액수가 부족함)
//                .build();
//        CommunityEntity foundCommunity = communityRepository.findByAccountNumber("1468299555144")
//                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));
//
//        // when
//        accountTransferPort.createAccountTransfer(reqDto1);
//        accountTransferPort.createAccountTransfer(reqDto2);
//        CommunityFeeStatusReqDto reqDto3 = CommunityFeeStatusReqDto.builder()
//                .communityId(foundCommunity.getId())
//                .build();
//        CommunityFeeStatusRespDto result = communityService.checkFeeStatus(reqDto3);
//
//        // then
//        Assertions.assertThat(result.getPaidMembers().size()).isEqualTo(1); // 함형주만 납부 완료
//        Assertions.assertThat(result.getPaidMembers()).extracting("memberName").containsExactlyInAnyOrder(hj.getName());
//        Assertions.assertThat(result.getUnpaidMembers().size()).isEqualTo(2); // 최선정, 윤영헌은 미납
//
//    }

    @Test
    @DisplayName("모임의 회비 금액, 주기 수정")
    @Transactional
    //@WithMockUser(username = "01026530957")  // 로그인한 사용자 모킹
    public void changeFeeInfo() {
        // case 1 : 계주가 변경 시도
        // given
        MemberEntity hj = memberRepository.findByUsername("01026530957") // 함형주 : 계주
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        MemberEntity sj = memberRepository.findByUsername("01012345678") // 최선정 : 일반 회원
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        CommunityEntity foundCommunity = communityRepository.findByAccountNumber("1468299555144")
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다.")); // 모임1, 계주는 함형주

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(hj, null, hj.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context); // 함형주로 로그인

        CommunityChgFeeInfoReqDto reqDto = CommunityChgFeeInfoReqDto.builder()
                .communityId(foundCommunity.getId())
                .fee(30000L)
                .feePeriod(15L)
                .build();

        // when - then
        assertDoesNotThrow(() -> communityService.changeFeeInfo(reqDto));

        // then
        CommunityEntity updatedCommunity = communityRepository.findByAccountNumber("1468299555144")
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다.")); // 모임1, 계주는 함형주
        Assertions.assertThat(updatedCommunity.getFee()).isEqualTo(30000L);
        Assertions.assertThat(updatedCommunity.getFeePeriod()).isEqualTo(15L);

        // case 2 : 계주 아닌 일반 회원이 변경 시도
        // given
        UsernamePasswordAuthenticationToken authentication2 =
                new UsernamePasswordAuthenticationToken(sj, null, sj.getAuthorities());
        SecurityContext context2 = SecurityContextHolder.createEmptyContext();
        context2.setAuthentication(authentication2);
        SecurityContextHolder.setContext(context2); // 최선정으로 로그인

        CommunityChgFeeInfoReqDto reqDto2 = CommunityChgFeeInfoReqDto.builder()
                .communityId(foundCommunity.getId())
                .fee(40000L)
                .feePeriod(5L)
                .build();

        // when - then
        assertThrows(NoAuthorityException.class, () -> communityService.changeFeeInfo(reqDto2));
        // 허가되지 않은 회원이므로 예외가 발생해야 함

    }


}
