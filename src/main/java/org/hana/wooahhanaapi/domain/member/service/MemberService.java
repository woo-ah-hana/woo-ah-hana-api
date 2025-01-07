package org.hana.wooahhanaapi.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.dto.MemberDto;
import org.hana.wooahhanaapi.domain.member.dto.SignUpRequestDto;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.exception.UserNotFoundException;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

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
            throw new UserNotFoundException("중복된 아이디입니다.");
        }

        MemberEntity memberEntity = MemberEntity.create(
                request.getUsername(),
                request.getName(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber()
        );
        memberRepository.save(memberEntity);
        return memberEntity.getUsername();
    }

    public MemberDto getMemberInfo() {
        MemberEntity memberEntity1 = (MemberEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return MemberDto.builder()
                .username(memberEntity1.getUsername())
                .name(memberEntity1.getName())
                .phoneNumber(memberEntity1.getPhoneNumber())
                .build();
//        MemberEntity memberEntity2 = this.memberRepository.findAllByUsername("ham");
//        return memberEntity2.getUsername();
    }
}
