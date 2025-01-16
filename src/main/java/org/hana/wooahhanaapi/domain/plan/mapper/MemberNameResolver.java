package org.hana.wooahhanaapi.domain.plan.mapper;

import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MemberNameResolver {
    private final MemberRepository memberRepository;

    public MemberNameResolver(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<String> resolveMemberNames(List<UUID> memberIds) {
        return memberIds.stream()
                .map(this::getMemberName)
                .collect(Collectors.toList());
    }

    private String getMemberName(UUID memberId) {
        return memberRepository.findById(memberId)
                .map(MemberEntity::getName)
                .orElse("Unknown");
    }
}

