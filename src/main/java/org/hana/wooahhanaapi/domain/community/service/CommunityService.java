package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.account.dto.*;
import org.hana.wooahhanaapi.account.port.AccountTransferPort;
import org.hana.wooahhanaapi.account.port.AccountTransferRecordPort;
import org.hana.wooahhanaapi.account.port.GetAccountInfoPort;
import org.hana.wooahhanaapi.account.exception.MemberNotPresentException;
import org.hana.wooahhanaapi.domain.community.domain.AutoDeposit;
import org.hana.wooahhanaapi.domain.community.entity.AutoDepositEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.exception.*;
import org.hana.wooahhanaapi.domain.community.mapper.AutoDepositMapper;
import org.hana.wooahhanaapi.domain.community.repository.AutoDepositRepository;
import org.hana.wooahhanaapi.domain.plan.dto.GetMembersResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.hana.wooahhanaapi.redis.ValidateAccountPort;
import org.hana.wooahhanaapi.redis.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.redis.SaveValidCodePort;
import org.hana.wooahhanaapi.redis.dto.SendValidationCodeReqDto;
import org.hana.wooahhanaapi.account.exception.IncorrectValidationCodeException;
import org.hana.wooahhanaapi.domain.community.dto.*;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final AutoDepositRepository autoDepositRepository;
    private final GetAccountInfoPort getAccountInfoPort;
    private final PlanRepository planRepository;
    private final PostRepository postRepository;

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

        CommunityEntity createdCommunity = communityRepository.save(newCommunity);
        MembershipEntity newMembership = MembershipEntity.create(userDetails, createdCommunity);
        membershipRepository.save(newMembership);

    }

     // 모임 생성시 계좌 인증 1원 보내기
    public void sendValidationCode(SendValidationCodeReqDto sendValidationCodeReqDto) {
        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(100, 1000);
        SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                .accountNumber(sendValidationCodeReqDto.getAccountNumber())
                .bankTranId(sendValidationCodeReqDto.getBankTranId())
                .inoutType("입금")
                .printContent(validCode)
                .tranAmt("1")
                .build();
        accountTransferPort.createAccountTransfer(reqDto);
        saveValidCodePort.saveValidCode(sendValidationCodeReqDto.getAccountNumber(),validCode);
    }

    // 계주 변경
    public void changeCommunityManager(CommunityChgManagerReqDto dto) {

        // 모임 찾기
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 멤버 찾기
        MemberEntity foundMember = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 지정하고자 하는 멤버가 그 커뮤니티의 멤버인지 확인
        boolean isMember = membershipRepository.existsByMemberAndCommunity(foundMember, foundCommunity);
        if (!isMember) {
            throw new NotAMemberException("해당 회원은 모임의 멤버가 아닙니다.");
        }

        // 관리자 변경 (기존 커뮤니티의 관리자 변경)
        foundCommunity.changeManagerId(foundMember.getId()); // 'setManager' 메서드는 커뮤니티의 관리자 속성을 변경한다고 가정

        // 모임 저장
        communityRepository.save(foundCommunity);

    }

    // 회비 납입 여부 체크
    @Transactional
    public CommunityFeeStatusRespDto checkFeeStatus(CommunityFeeStatusReqDto dto) {
        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));
        List<MemberEntity> members = membershipRepository.findMembersByCommunityId(dto.getCommunityId());

        Long fee = foundCommunity.getFee();
        // 현재 년, 월, 일 추출
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();  // 년도
        int month = now.getMonthValue();  // 월 (1-12)

        String str_month = "";
        if(month < 10) {
            str_month = "0" + month; // 두 자리수로 맞추어 주어야 함
        }

        int lastDay = YearMonth.now().lengthOfMonth(); // 오늘 달의 말일
        String fromDate = year + "-" + str_month + "-" + "01"; // xx월 1일부터
        String toDate = year + "-" + str_month + "-" + lastDay; // xx월 말일까지 조회

        System.out.println(fromDate);
        System.out.println(toDate);
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

        // 일단 멤버 전체에 대해 누적 입금액 저장할 원소 추가
        for(MemberEntity m : members) {
            memberPayments.put(m, 0L);
        }

        for (AccountTransferRecordRespListDto listDto : resultData) {
            if ("입금".equals(listDto.getInoutType())) {
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
                paidMembers.add(new CommunityFeeStatusRespListDto(entry.getKey().getId(), entry.getKey().getName(), entry.getValue()));
            }
            else {
                unpaidMembers.add(new CommunityFeeStatusRespListDto(entry.getKey().getId(), entry.getKey().getName(), entry.getValue()));
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
        // 모임 이름
        String communityAccountName = foundCommunity.getName();
        // 모임통장 계좌번호
        String communityAccountNumber = foundCommunity.getAccountNumber();

        return new CommunityDepositInfoRespDto(bankTranId, memberAccountNumber, memberAccountBalance, communityAccountBank, communityAccountName, communityAccountNumber);
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
            SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                    .accountNumber(userDetails.getAccountNumber())
                    .bankTranId(userDetails.getBankTranId())
                    .inoutType("출금")
                    .printContent(foundCommunity.getName())
                    .tranAmt(dto.getAmount())
                    .build();
            accountTransferPort.createAccountTransfer(reqDto);
        }
        catch (Exception e){
            throw new RuntimeException("개인 계좌에서 출금에 실패했습니다.");
        }
        // 모임통장에 입금
        try{
            SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                    .accountNumber(foundCommunity.getAccountNumber())
                    .bankTranId("001")
                    .inoutType("입금")
                    .printContent(userDetails.getName())
                    .tranAmt(dto.getAmount())
                    .build();
            accountTransferPort.createAccountTransfer(reqDto);
        }catch (Exception e){
            throw new RuntimeException("모임 계좌 입금에 실패했습니다.");
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

    // 개인 계좌 변경
    public void changeMemberAccount(CommunityChgMemAccReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 계좌 인증 절차
        AccountValidationConfirmDto accValidDto = AccountValidationConfirmDto.builder()
                .accountNumber(dto.getAccountNumber())
                .validationCode(dto.getValidationCode())
                .build();
        if(!validateAccountPort.validateAccount(accValidDto)) {
            throw new IncorrectValidationCodeException("입금자명이 일치하지 않습니다.");
        }
        // 이전 개인 계좌
        String prevAccountNum = userDetails.getAccountNumber();

        // 계좌 수정
        userDetails.updateAccount(dto.getAccountNumber(), dto.getBankTranId());

        // 기존 계좌 자동이체 해제
        List<AutoDepositEntity> prevAutoDeposits = autoDepositRepository.findAllByMemberAccNum(prevAccountNum);
        autoDepositRepository.deleteAll(prevAutoDeposits);

        // 저장
        memberRepository.save(userDetails);
    }
  
    // 모임의 회비 금액, 주기 수정
    public void changeFeeInfo(CommunityChgFeeInfoReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        System.out.println(userDetails.getId());
        // 현재 로그인 유저가 계주가 아닐 때 => 권한 없음
        if(!userDetails.getId().equals(foundCommunity.getManagerId())) {
            throw new NoAuthorityException("권한이 없습니다.");
        }

        foundCommunity.updateFeeInfo(dto.getFee(), dto.getFeePeriod());

        communityRepository.save(foundCommunity);

    }

    // 자동이체 설정 정보 저장
    public void setAutoDeposit(CommunityAutoDepositReqDto dto) {
        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 모임 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        if(autoDepositRepository.existsByCommunityAccNumAndMemberAccNum(
                foundCommunity.getAccountNumber(), userDetails.getAccountNumber())) {
            throw new DuplicateAutoDepositException("이미 자동이체가 등록되어 있습니다.");
        }

        AutoDeposit newAutoDeposit = AutoDeposit.create(
                null,
                userDetails.getBankTranId(),
                userDetails.getAccountNumber(),
                foundCommunity.getAccountNumber(),
                dto.getFee(),
                dto.getDepositDay()
        );

        // 자동이체 정보 저장
        autoDepositRepository.save(AutoDepositMapper.mapDomainToEntity(newAutoDeposit));
    }

    // 자동이체 설정 해제
    public void deleteAutoDeposit(UUID communityId) {

        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        CommunityEntity foundCommunity = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        try{
            AutoDepositEntity autoDepositEntity = autoDepositRepository.findByCommunityAccNumAndAndMemberAccNumAndMemberBankTranId(
                    foundCommunity.getAccountNumber(),userDetails.getAccountNumber(),userDetails.getBankTranId());
            autoDepositRepository.delete(autoDepositEntity);
        } catch (Exception e) {
            throw new AutoDepositNotFoundException("설정된 자동이체 내역이 없습니다.");
        }
    }

    // 매일 자정에 당일이 자동이체 날짜인 계좌들을 체크 -> 이체 진행
    @Scheduled(cron = "0 0 0 * * ?")
    public void sendDailyTransfers() {
        // 오늘 날짜
        LocalDate today = LocalDate.now();
        int todayDay = today.getDayOfMonth();  // 오늘 날짜의 일(day)

        // 이체 날짜가 오늘인 계좌들 조회
        List<AutoDepositEntity> accounts = autoDepositRepository.findALlByDepositDay(todayDay);
        // 계좌 별 이체 진행
        for (AutoDepositEntity account : accounts) {
            // 모임 찾고
            CommunityEntity foundCommunity = communityRepository.findByAccountNumber(account.getCommunityAccNum())
                    .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));
            // 멤버 찾고
            MemberEntity foundMember = memberRepository.findByAccountNumber(account.getMemberAccNum())
                    .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
            // 멤버 개인 통장에서 먼저 출금
            try {
                SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                        .accountNumber(account.getMemberAccNum())
                        .bankTranId(account.getMemberBankTranId())
                        .inoutType("출금")
                        .printContent(foundCommunity.getName())
                        .tranAmt(account.getFee())
                        .build();
                accountTransferPort.createAccountTransfer(reqDto);
            } catch (Exception e) {
                throw new RuntimeException("개인 계좌에서 출금에 실패했습니다.");
            }
            // 모임통장에 입금
            try{
                SimplifiedTransferReqDto reqDto = SimplifiedTransferReqDto.builder()
                        .accountNumber(account.getCommunityAccNum())
                        .bankTranId("001")
                        .inoutType("입금")
                        .printContent(foundMember.getName())
                        .tranAmt(account.getFee())
                        .build();
                accountTransferPort.createAccountTransfer(reqDto);
            }catch (Exception e){
                throw new RuntimeException("모임 계좌 입금에 실패했습니다.");
            }
        }
    }
    
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

    public List<GetMembersResponseDto> getMembers(UUID communityId) {
        List<MemberEntity> foundMembers = membershipRepository.findMembersByCommunityId(communityId);
        return foundMembers.stream().map(
                member -> GetMembersResponseDto.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .build()).toList();
    }

    public RegisterInCommunityResponseDto registerInCommunity(RegisterInCommunityRequestDto requestDto) {
        MemberEntity memberEntity = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new MemberNotPresentException("가입된 멤버가 없습니다."));
        CommunityEntity communityEntity = communityRepository.findById(requestDto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("모임 통장이 존재하지 않습니다."));
        membershipRepository.save(MembershipEntity.create(memberEntity, communityEntity));
        return RegisterInCommunityResponseDto.builder()
                .communityName(communityEntity.getName())
                .memberName(memberEntity.getName())
                .success(true)
                .build();
    }

    public CommunityFullInfoResponseDto getCommunityFullInfo(UUID communityId) {

        // 모임 불러오기
        CommunityEntity community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        // 모임 전체 정보 반환
        return CommunityFullInfoResponseDto.builder()
                .id(community.getId())
                .managerId(community.getManagerId())
                .name(community.getName())
                .accountNumber(community.getAccountNumber())
                .credits(community.getCredits())
                .fee(community.getFee())
                .feePeriod(community.getFeePeriod())
                .build();

    }

    public void quitFromCommunity(UUID communityId) {
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 모임 불러오기
        CommunityEntity community = communityRepository.findById(communityId)
                .orElseThrow(() -> new CommunityNotFoundException("모임을 찾을 수 없습니다."));

        if(membershipRepository.existsByMemberAndCommunity(userDetails, community)){
            membershipRepository.deleteByMemberAndCommunity(userDetails, community);
        }
        else {
            throw new NotAMemberException("해당 회원은 모임의 멤버가 아닙니다.");
        }
    }

    public GetExpenseInfoRespDto getExpenseInfo(GetExpenseInfoReqDto getExpenseInfoReqDto) {
        CommunityEntity community = communityRepository.findById(getExpenseInfoReqDto.getCommunityId()).orElseThrow(()->new CommunityNotFoundException("모임 정보를 찾을 수 없습니다."));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localFromDate = LocalDate.parse(getExpenseInfoReqDto.getFromDate(), formatter);
        LocalDate localToDate = LocalDate.parse(getExpenseInfoReqDto.getToDate(), formatter);

        LocalDate localPreviousFromDate = localFromDate.minusMonths(3);
        LocalDate localPreviousToDate = localToDate.minusMonths(3);

        String previousFromDate = localPreviousFromDate.format(formatter);
        String previousToDate = localPreviousToDate.format(formatter);

        AccountTransferRecordReqDto thisQuarterReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001")
                .accountNumber(community.getAccountNumber())
                .fromDate(getExpenseInfoReqDto.getFromDate())
                .toDate(getExpenseInfoReqDto.getToDate())
                .build();
        AccountTransferRecordRespDto thisQuarterResult = accountTransferRecordPort.getTransferRecord(thisQuarterReqDto);

        AccountTransferRecordReqDto lastQuarterReqDto = AccountTransferRecordReqDto.builder()
                .bankTranId("001")
                .accountNumber(community.getAccountNumber())
                .fromDate(previousFromDate)
                .toDate(previousToDate)
                .build();
        AccountTransferRecordRespDto lastQuarterResult = accountTransferRecordPort.getTransferRecord(lastQuarterReqDto);

        long thisQuarterExpense = thisQuarterResult.getData().getResList().stream()
                .filter(transfer -> "출금".equals(transfer.getInoutType()))
                .mapToLong(transfer -> Long.parseLong(transfer.getTranAmt()))
                .sum();

        long thisQuarterIncome = thisQuarterResult.getData().getResList().stream()
                .filter(transfer -> "입금".equals(transfer.getInoutType()))
                .mapToLong(transfer -> Long.parseLong(transfer.getTranAmt()))
                .sum();

        long lastQuarterExpense = lastQuarterResult.getData().getResList().stream()
                .filter(transfer -> "출금".equals(transfer.getInoutType()))
                .mapToLong(transfer -> Long.parseLong(transfer.getTranAmt()))
                .sum();

        LocalDateTime fromDateTime = localFromDate.atStartOfDay();
        LocalDateTime toDateTime = localToDate.atTime(23, 59, 59);

        List<PlanEntity> planList = planRepository.findPlansInPeriod(getExpenseInfoReqDto.getCommunityId(),fromDateTime,toDateTime);
        List<GetExpensePlanInfoDto> planInfoList = planList.stream()
                .map(plan -> GetExpensePlanInfoDto.builder()
                        .title(plan.getTitle())
                        .category(plan.getCategory())
                        .build())
                .toList();

        List<Long> monthlyExpenses = new ArrayList<>(Collections.nCopies(3, 0L));
        for (AccountTransferRecordRespListDto transfer : thisQuarterResult.getData().getResList()) {
            if ("출금".equals(transfer.getInoutType())) {
                LocalDate transferDate = LocalDate.parse(transfer.getTranDate(), formatter);
                int monthIndex = transferDate.getMonthValue() - localFromDate.getMonthValue();
                if (monthIndex >= 0 && monthIndex < 3) {
                    monthlyExpenses.set(monthIndex, monthlyExpenses.get(monthIndex) + Long.parseLong(transfer.getTranAmt()));
                }
            }
        }

        int highestMonth = IntStream.range(0, 3)
                .boxed()
                .max(Comparator.comparingLong(monthlyExpenses::get))
                .orElse(0);

        highestMonth = localFromDate.getMonthValue() + highestMonth;
        Map<PlanEntity, Long> planExpenses = new HashMap<>();
        for (PlanEntity plan : planList) {
            long planExpense = thisQuarterResult.getData().getResList().stream()
                    .filter(transfer -> {
                        LocalDate tranDate = LocalDate.parse(transfer.getTranDate());
                        LocalDate startDate = plan.getStartDate().toLocalDate();
                        LocalDate endDate = plan.getEndDate().toLocalDate();

                        return "출금".equals(transfer.getInoutType()) && !tranDate.isBefore(startDate) && !tranDate.isAfter(endDate);
                    })
                    .mapToLong(transfer -> Long.parseLong(transfer.getTranAmt()))
                    .sum();

            planExpenses.put(plan, planExpense);
        }

        PlanEntity highestPlan = planExpenses.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse(null);
        String highestPlanName = null;
        if(highestPlan != null) {
            highestPlanName = highestPlan.getTitle();
        }

        Long highestPlanExpense = planExpenses.getOrDefault(highestPlan, 0L);

        PostEntity post = postRepository.findFirstByPlan(highestPlan);
        String imageUrl = (post != null) ? post.getImageUrl() : null;

        return GetExpenseInfoRespDto.builder()
                .planInfoList(planInfoList)
                .numberOfPlans(planList.size())
                .howMuchSpentThanLastQuarter(thisQuarterExpense - lastQuarterExpense)
                .thisQuarterExpense(thisQuarterExpense)
                .thisQuarterIncome(thisQuarterIncome)
                .highestMonth(highestMonth)
                .monthlyExpenses(monthlyExpenses)
                .highestPlanName(highestPlanName)
                .highestPlanExpense(highestPlanExpense)
                .imageUrl(imageUrl)
                .build();
    }
}
