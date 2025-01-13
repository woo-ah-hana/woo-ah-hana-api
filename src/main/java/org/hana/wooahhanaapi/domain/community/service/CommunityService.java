package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountTransferReqDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountValidationReqDto;
import org.hana.wooahhanaapi.domain.account.entity.AccountValidationEntity;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.exception.IncorrectValidationCodeException;
import org.hana.wooahhanaapi.domain.account.repository.AccountValidationRepository;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.community.dto.CommunityCreateReqDto;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final AccountValidationRepository accountValidationRepository;
    private final AccountService accountService;

    // 모임 생성
    public void createCommunity(CommunityCreateReqDto dto) {

        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AccountValidationConfirmDto accValidDto = AccountValidationConfirmDto.builder()
                .accountNumber(dto.getAccountNumber())
                .validationCode(dto.getValidationCode())
                .build();

        if(!validateAccountConfirm(accValidDto)) {
            throw new IncorrectValidationCodeException("입금자명이 일치하지 않습니다.");
        }

        CommunityEntity newCommunity = new CommunityEntity(
                null,
                userDetails.getId(),
                dto.getName(),
                dto.getAccountNumber(),
                dto.getCredits(),
                dto.getFee(),
                dto.getFeePeriod());

        communityRepository.save(newCommunity);

    }

     // 모임 생성시 계좌 인증
    public void validateAccountRequest(AccountValidationReqDto dto) {
        // 랜덤한 유효 코드 생성 (숫자 형식으로)
        String validCode = "우아하나" + ThreadLocalRandom.current().nextInt(1000);
        AccountValidationEntity ventity = new AccountValidationEntity(
                null,
                dto.getAccountNumber(),
                validCode,
                System.currentTimeMillis() + 5 * 60 * 1000
        );
        accountValidationRepository.save(ventity);
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");

        // 날짜를 원하는 형식의 String으로 변환
        String formattedDate = currentDate.format(formatter1);
        String formattedTime = currentDate.format(formatter2);
        AccountTransferReqDto dto2 = AccountTransferReqDto.builder()
                        .accountNumber(dto.getAccountNumber())
                        .tranDate(formattedDate)
                        .tranTime(formattedTime)
                        .inoutType("입금")
                        .tranType("결재")
                        .printContent(validCode)
                        .tranAmt("1")
                        .branchName("우아하나")
                        .build();
        System.out.println(dto2.getAccountNumber());
        accountService.createTransfer(dto2);
    }

    // 계좌 인증 중 입금자명 검증
    public boolean validateAccountConfirm(AccountValidationConfirmDto dto) {

        // 입금자명 저장한 엔티티 불러오기
        AccountValidationEntity foundValidEntity = accountValidationRepository.findByAccountNumber(dto.getAccountNumber());
        if (foundValidEntity != null) {
            // 입금자명 검증
            if(foundValidEntity.getValidationCode().equals(dto.getValidationCode())
                    && System.currentTimeMillis() < foundValidEntity.getExpirationTime()) {
                return true;
            }
            else {
                return false;
            }
        }
        // 입금자명이 저장이 안되어 있으면
        else {
            throw new AccountNotFoundException("계좌를 찾을 수 없습니다.");
        }
    }
}
