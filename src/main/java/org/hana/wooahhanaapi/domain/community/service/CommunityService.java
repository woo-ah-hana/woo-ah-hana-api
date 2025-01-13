package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.community.adapter.ValidateAccountPort;
import org.hana.wooahhanaapi.domain.community.adapter.dto.AccountValidationConfirmDto;
import org.hana.wooahhanaapi.domain.community.adapter.SendValidCodePort;
import org.hana.wooahhanaapi.domain.community.adapter.dto.SendValidationCodeReqDto;
import org.hana.wooahhanaapi.domain.account.exception.AccountNotFoundException;
import org.hana.wooahhanaapi.domain.account.exception.IncorrectValidationCodeException;
import org.hana.wooahhanaapi.domain.community.domain.Community;
import org.hana.wooahhanaapi.domain.community.dto.CommunityChgManagerReqDto;
import org.hana.wooahhanaapi.domain.community.dto.CommunityCreateReqDto;
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

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final SendValidCodePort sendValidCodePort;
    private final ValidateAccountPort validateAccountPort;

    // 모임 생성
    public void createCommunity(CommunityCreateReqDto dto) {

        // 현재 로그인한 사용자 정보 가져오기
        MemberEntity userDetails = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        AccountValidationConfirmDto accValidDto = AccountValidationConfirmDto.builder()
                .accountNumber(dto.getAccountNumber())
                .validationCode(dto.getValidationCode())
                .build();
        System.out.println("aa");
        if(!validateAccountPort.validateAccount(accValidDto)) {
            throw new IncorrectValidationCodeException("입금자명이 일치하지 않습니다.");
        }
        System.out.println("dd");
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
        sendValidCodePort.sendValidCode(sendValidationCodeReqDto);
    }

    public boolean changeCommunityManager(CommunityChgManagerReqDto dto) {

        // 커뮤니티 찾고
        CommunityEntity foundCommunity = communityRepository.findById(dto.getCommunityId())
                .orElseThrow(() -> new CommunityNotFoundException("커뮤니티를 찾을 수 없습니다."));

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
}
