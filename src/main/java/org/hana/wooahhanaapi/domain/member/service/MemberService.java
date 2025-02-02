package org.hana.wooahhanaapi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.port.GetAccountInfoPort;
import org.hana.wooahhanaapi.domain.account.dto.GetAccountInfoReqDto;
import org.hana.wooahhanaapi.domain.account.exception.MemberNotPresentException;
import org.hana.wooahhanaapi.domain.member.dto.*;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.*;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final GetAccountInfoPort getAccountInfoPort;

    private static final String passwordPattern = "^(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[a-zA-Z\\d!@#$%^&*]{8,}$";
    private static final Pattern pattern = Pattern.compile(passwordPattern);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        return memberEntity;
    }

    public String signUp(SignUpRequestDto request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("중복된 아이디입니다.");
        }

        Matcher matcher = pattern.matcher(request.getPassword());
        if (!matcher.matches()) {
            throw new PasswordNotSatisfyCondException("비밀번호 조건을 만족하지 않습니다.");
        }

        MemberEntity memberEntity = MemberEntity.create(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber(),
                request.getAccountNumber(),
                request.getBankTranId(),
                request.getFcmToken()
        );
        memberRepository.save(memberEntity);
        return memberEntity.getUsername();
    }

    public MemberResponseDto getMemberInfo() {
        try{
            MemberEntity memberEntity1 = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return MemberResponseDto.builder()
                    .username(memberEntity1.getUsername())
                    .name(memberEntity1.getName())
                    .phoneNumber(memberEntity1.getPhoneNumber())
                    .build();
        }catch (Exception e){
            throw new UserNotLoginException("로그인이 안되어 있습니다. 로그인 해주세요.");
        }
    }
    public String getMemberName(UUID uuid) {
        try{
            return memberRepository.findById(uuid).get().getName();
        } catch (Exception e) {
            throw new MemberNotPresentException("id에 해당하는 member가 없습니다");
        }
    }

    public String logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            SecurityContextHolder.clearContext();
            return "로그아웃 성공";
        } else {
            throw new UserNotLoginException("로그인이 되어 있지 않습니다.");
        }
    }

    public MyAccountResponseDto getMyAccountInfo() {
        try{
            MemberEntity memberEntity = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            GetAccountInfoReqDto reqDto = new GetAccountInfoReqDto(memberEntity.getBankTranId(),"00","2025-01-17",memberEntity.getAccountNumber());
            return MyAccountResponseDto.builder()
                    .accountNumber(String.valueOf(memberEntity.getAccountNumber()))
                    .bankTranId(String.valueOf(memberEntity.getBankTranId()))
                    .name(memberEntity.getName())
                    .amount(getAccountInfoPort.getAccountInfo(reqDto).getData().getBalanceAmt())
                    .build();
        }catch (Exception e){
            throw new UserNotLoginException("로그인이 안되어 있습니다. 로그인 해주세요.");
        }
    }

    public String changePassword(ChangePasswordReqDto reqDto) {
        MemberEntity memberEntity = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Matcher matcher = pattern.matcher(reqDto.getNewPassword());
        if(matcher.matches()) {
            memberEntity.updatePassword(passwordEncoder.encode(reqDto.getNewPassword()));
            memberRepository.save(memberEntity);
        }
        else {
            throw new PasswordNotSatisfyCondException("비밀번호 조건을 만족하지 않습니다.");
        }

        return memberEntity.getUsername();

    }

    public InquiryMemberRespDto inquiryMember(String id) {
        try{
            MemberEntity memberEntity = memberRepository.findByUsername(id).orElseThrow();
            return InquiryMemberRespDto.builder()
                    .id(memberEntity.getId().toString())
                    .name(memberEntity.getName())
                    .build();
        }catch (Exception e){
            return InquiryMemberRespDto.builder()
                    .id("존재하지 않습니다.")
                    .name("존재하지 않습니다.")
                    .build();
        }
    }
}
