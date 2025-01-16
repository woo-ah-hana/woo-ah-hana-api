package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferPort;
import org.hana.wooahhanaapi.domain.account.adapter.AccountTransferRecordPort;
import org.hana.wooahhanaapi.domain.account.adapter.GetAccountInfoPort;
import org.hana.wooahhanaapi.domain.account.adapter.dto.*;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.community.exception.NoAuthorityException;
import org.hana.wooahhanaapi.utils.redis.ValidateAccountPort;
import org.hana.wooahhanaapi.utils.redis.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.utils.redis.SaveValidCodePort;
import org.hana.wooahhanaapi.utils.redis.dto.SendValidationCodeReqDto;
import org.hana.wooahhanaapi.domain.account.exception.IncorrectValidationCodeException;
import org.hana.wooahhanaapi.domain.community.domain.Community;
import org.hana.wooahhanaapi.domain.community.dto.*;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.exception.NotAMemberException;
import org.hana.wooahhanaapi.domain.community.mapper.CommunityMapper;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final SaveValidCodePort saveValidCodePort;
    private final ValidateAccountPort validateAccountPort;
    private final AccountTransferPort accountTransferPort;
    private final AccountTransferRecordPort accountTransferRecordPort;
    private final GetAccountInfoPort getAccountInfoPort;

    // 모임 생성
    public void createCommunity(CommunityCreateReqDto dto) {

        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AccountValidationConfirmDto accValidDto = AccountValidationConfirmDto.builder()
                .accountNumber(dto.getAccountNumber())
                .validationCode(dto.getValidationCode())
                .build();
        if(!validateAccountPort.validateAccount(accValidDto)) {
            throw new IncorrectValidationCodeException("입금자명이 일치하지 않습니다.");
        }
        CommunityEntity newCommunity = CommunityEntity.create(
                userDetails.getId(),
                dto.getName(),
                dto.getAccountNumber(),
                dto.getCredits(),
                dto.getFee(),
                dto.getFeePeriod());

        communityRepository.save(newCommunity);

    }

     // 모임 생성시 계좌 인증 1원 보내기
    public void sendValidationCode(SendValidationCodeReqDto sendValidationCodeReqDto) {
        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        try{
            // 날짜를 원하는 형식의 String으로 변환
            String formattedDate = currentDate.format(formatter1);
            String formattedTime = currentDate.format(formatter2);
            AccountTransferReqDto dto2 = AccountTransferReqDto.builder()
                    .accountNumber(sendValidationCodeReqDto.getAccountNumber())
                    .bankTranId(sendValidationCodeReqDto.getBankTranId())//하나은행:001, 우리은행:002
                    .printContent(validCode)
                    .tranDate(formattedDate)
                    .tranTime(formattedTime)
                    .inoutType("입금")
                    .tranType("결재")
                    .tranAmt("1")
                    .branchName("우아하나")
                    .build();
            accountTransferPort.createAccountTransfer(dto2);
            saveValidCodePort.saveValidCode(sendValidationCodeReqDto.getAccountNumber(),validCode);
        }catch (Exception e){
            throw new AccountNotFoundException("계좌를 찾을 수 없음");
        }
    }

    // 계주 변경
    public boolean changeCommunityManager(CommunityChgManagerReqDto dto) {

        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 멤버 찾고
        MemberEntity foundMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 지정하고자 하는 멤버가 그 커뮤니티의 멤버인지 확인
        if(!membershipRepository.findAllByMemberAndCommunity(foundMember, foundCommunity).isEmpty()) {
            Community community = Community.create(
                    foundCommunity.getId(), dto.getMemberId(), foundCommunity.getName(),
                    foundCommunity.getAccountNumber(), foundCommunity.getCredits(), foundCommunity.getFee(), foundCommunity.getFeePeriod()
            );
            communityRepository.save(CommunityMapper.mapDomainToEntity(community));
            return true;
        }
        else {
            throw new NotAMemberException("해당 회원은 모임의 멤버가 아닙니다.");
        }

    }

    // 회비 납입 여부 체크
    public CommunityFeeStatusRespDto checkFeeStatus(CommunityFeeStatusReqDto dto) {
        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));
        List<MemberEntity> members = membershipRepository.findMembersByCommunityId(dto.getCommunityId());

        Long fee = foundCommunity.getFee();
        // 매월 입금날 찾기
        Long feePeriod = foundCommunity.getFeePeriod();
        // 현재 년, 월, 일 추출
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();  // 년도
        int month = now.getMonthValue();  // 월 (1-12)
        int dayOfMonth = now.getDayOfMonth();  // 일 (1-31)
        String fromDate = "";
        String toDate = "";

        if(feePeriod > dayOfMonth) {
            if(month-1 == 0) {
                fromDate = (year-1) + "-" + 12 + "-" + feePeriod;
            }
            else {
                fromDate = year + "-" + (month-1) + "-" + feePeriod;
            }
            toDate = year + "-" + month + "-" + dayOfMonth;

        }
        else if(feePeriod < dayOfMonth) {
            fromDate = year + "-" + month + "-" + feePeriod;
            toDate = year + "-" + month + "-" + dayOfMonth;
        }

        // 조회 조건 dto 생성
        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001")
                .accountNumber(foundCommunity.getAccountNumber())
                .fromDate(fromDate)
                .toDate(toDate)
                .build();

        Set<CommunityFeeStatusRespListDto> paidMembers = new HashSet<>();  // 납부한 멤버
        Set<CommunityFeeStatusRespListDto> unpaidMembers = new HashSet<>();  // 납부하지 않은 멤버

        // 멤버별 입금액을 누적할 맵
        Map<MemberEntity, Long> memberPayments = new HashMap<>();

        // 기록 조회
        List<AccountTransferRecordRespListDto> resultData = accountTransferRecordPort.getTransferRecord(reqDto)
                .getData().getResList();

        for (AccountTransferRecordRespListDto listDto : resultData) {
            if ("입금".equals(listDto.getTranType())) {
                // 입금 내역의 계좌 번호와 매핑된 멤버를 찾아서 입금액 누적
                for (MemberEntity member : members) {
                    if (member.getName().equals(listDto.getPrintContent())) {
                        // 입금액 누적
                        memberPayments.put(member,
                                memberPayments.getOrDefault(member, 0L) + Long.valueOf(listDto.getTranAmt()));
                    }
                }
            }
        }

        for (Map.Entry<MemberEntity, Long> entry : memberPayments.entrySet()) {
            // 매월 입금액을 만족했으면 납입한 멤버 목록에 추가
            if (entry.getValue().compareTo(fee) >= 0) {
                paidMembers.add(new CommunityFeeStatusRespListDto(entry.getKey().getName(), entry.getValue()));
            }
            else {
                unpaidMembers.add(new CommunityFeeStatusRespListDto(entry.getKey().getName(), entry.getValue()));
            }
        }

        return new CommunityFeeStatusRespDto(paidMembers, unpaidMembers);

    }

    // 모임통장에 입금 위한 정보 불러오기
    public CommunityDepositInfoRespDto depositToAccountInfo(CommunityDepositInfoReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 모임 불러오기
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 멤버 개인 계좌의 은행
        String bankTranId = userDetails.getBankTranId();
        // 멤버 개인 계좌번호
        String memberAccountNumber = userDetails.getAccountNumber();
        //멤버 계좌 잔액
        GetAccountInfoReqDto getAccountInfoReqDto = new GetAccountInfoReqDto(bankTranId,"00","2025-01-17",memberAccountNumber);
        Long memberAccountBalance = getAccountInfoPort.getAccountInfo(getAccountInfoReqDto).getData().getBalanceAmt() ;
        // 모임통장 계좌의 은행
        String communityAccountBank = "하나은행";
        // 모임통장 계좌번호
        String communityAccountNumber = foundCommunity.getAccountNumber();

        return new CommunityDepositInfoRespDto(bankTranId, memberAccountNumber, memberAccountBalance, communityAccountBank, communityAccountNumber);
    }

    // 모임통장에 입금
    public void depositToAccount(CommunityDepositReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 모임 불러오기
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 멤버 개인 통장에서 먼저 출금
        try{
            transfer(userDetails.getAccountNumber(), "001", userDetails.getName(), "출금", dto.getAmount());
        }
        catch (Exception e){
            throw new RuntimeException("개인 계좌에서 출금에 실패했습니다.");
        }

        // 모임통장에 입금
        transfer(foundCommunity.getAccountNumber(), "001", foundCommunity.getName(), "입금", dto.getAmount());
    }

    // 입출금 메서드를 분리
    private void transfer(String accountNumber, String bankTranId, String printContent, String inoutType, String tranAmt) {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
        // 날짜를 원하는 형식의 String으로 변환
        String formattedDate = currentDate.format(formatter1);
        String formattedTime = currentDate.format(formatter2);
        try {
            AccountTransferReqDto dto2 = AccountTransferReqDto.builder()
                    .accountNumber(accountNumber)
                    .bankTranId(bankTranId)//하나은행:001, 우리은행:002
                    .printContent(printContent)
                    .tranDate(formattedDate)
                    .tranTime(formattedTime)
                    .inoutType(inoutType)
                    .tranType("결재")
                    .tranAmt(tranAmt)
                    .branchName("우아하나")
                    .build();
            accountTransferPort.createAccountTransfer(dto2);
        } catch (Exception e) {
            throw new AccountNotFoundException("계좌를 찾을 수 없음");
        }
    }
      
    // 모임통장 거래내역 조회
    public List<CommunityTrsfRecordRespDto> getTransferRecord(CommunityTrsfRecordReqDto dto) {

        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 모임에 등록된 모임통장 계좌번호 가져오기
        String communityAccountNumber = foundCommunity.getAccountNumber();

        // 최근 n개월까지 조회(예 : 1, 3, 6, 12개월)
        int duration = dto.getRecentMonth();
        LocalDateTime fromDate = LocalDateTime.now().minusMonths(duration);
        LocalDateTime toDate = LocalDateTime.now();

        // 날짜 포맷 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fDate = fromDate.format(formatter);
        String tDate = toDate.format(formatter);

        // 조회 조건 dto 생성
        AccountTransferRecordReqDto reqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001")
                .accountNumber(communityAccountNumber)
                .fromDate(fDate)
                .toDate(tDate)
                .build();

        // 기록 조회
        List<AccountTransferRecordRespListDto> resultData = accountTransferRecordPort.getTransferRecord(reqDto)
                .getData().getResList();

        List<CommunityTrsfRecordRespDto> records = resultData.stream()
                .map(record -> new CommunityTrsfRecordRespDto(
                        record.getTranDate(),
                        record.getTranTime(),
                        record.getInoutType(),
                        record.getTranType(),
                        record.getPrintContent(),
                        record.getTranAmt(),
                        record.getAfterBalanceAmt(),
                        record.getBranchName()
                )).collect(Collectors.toList());

        return records;
    }

    // 모임의 회비 금액, 주기 수정
    public void changeFeeInfo(CommunityChgFeeInfoReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("커뮤니티를 찾을 수 없습니다."));

        // 현재 로그인 유저가 계주가 아닐 때 => 권한 없음
        if(userDetails.getId() != foundCommunity.getManagerId()) {
            throw new NoAuthorityException("권한이 없습니다.");
        }

        Community foundCommunityDomain = CommunityMapper.mapEntityToDomain(foundCommunity);
        foundCommunityDomain.updateFeeInfo(dto.getFee(), dto.getFeePeriod());
        CommunityEntity editedCommunity = CommunityMapper.mapDomainToEntity(foundCommunityDomain);

        communityRepository.save(editedCommunity);

    public List<CommunitiesResponseDto> getCommunities() {
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CommunityEntity> result = membershipRepository.findCommunitiesByMemberId(userDetails.getId());

        return result.stream()
                .map(communityEntity -> CommunitiesResponseDto.builder()
                        .communityId(communityEntity.getId())
                        .name(communityEntity.getName())
                        .build()).toList();
    }

    public CommunityInfoResponseDto getCommunityInfo(UUID communityId) {
        try{
            //모임 통장 정보
            CommunityEntity community = communityRepository.findById(communityId).orElseThrow();
            //모임 통장 잔액
            GetAccountInfoReqDto getAccountInfoReqDto = new GetAccountInfoReqDto("001","00","2025-01-17", community.getAccountNumber());
            Long balance = getAccountInfoPort.getAccountInfo(getAccountInfoReqDto).getData().getBalanceAmt();

            return CommunityInfoResponseDto.builder()
                .name(community.getName())
                .accountNumber(community.getAccountNumber())
                .balance(balance)
                .build();
        }catch (Exception e){
            throw new CommunityNotFoundException("모임 아이디를 찾을 수 없습니다.");
        }

    }
}
