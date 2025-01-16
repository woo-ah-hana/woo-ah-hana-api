package org.hana.wooahhanaapi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.exception.MemberNotPresentException;
import org.hana.wooahhanaapi.domain.member.dto.MemberResponseDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.DuplicateUsernameException;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.exception.UserNotLoginException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        return memberEntity;
    }

    public String signUp(SignUpRequestDto request) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("중복된 아이디입니다.");
        }

        MemberEntity memberEntity = MemberEntity.create(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber(),
                request.getAccountNumber(),
                request.getBankTranId()
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
            MemberEntity memberEntity1 = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new UserNotLoginException("로그인이 안되어 있습니다");
        }
        try{
            return memberRepository.findById(uuid).get().getName();
        } catch (Exception e) {
            throw new MemberNotPresentException("id에 해당하는 member가 없습니다");
        }
    }
}
