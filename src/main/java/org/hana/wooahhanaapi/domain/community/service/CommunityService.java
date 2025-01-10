package org.hana.wooahhanaapi.domain.community.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.account.adapter.dto.AccountCreateReqDto;
import org.hana.wooahhanaapi.domain.account.service.AccountService;
import org.hana.wooahhanaapi.domain.community.dto.CommunityCreateReqDto;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    public void createCommunity(CommunityCreateReqDto dto) {

        CommunityEntity newCommunity = new CommunityEntity(
                null,
                dto.getManagerId(),
                dto.getName(),
                dto.getAccountNumber(),
                dto.getCredits(),
                dto.getFee(),
                dto.getFeePeriod());
        communityRepository.save(newCommunity);

    }
}
