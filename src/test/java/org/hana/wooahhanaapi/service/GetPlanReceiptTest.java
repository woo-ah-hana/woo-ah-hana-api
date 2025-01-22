package org.hana.wooahhanaapi.service;

import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.dto.GetReceiptResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class GetPlanReceiptTest {

    @Autowired
    private PlanService planService;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private CommunityRepository communityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    private MemberEntity member1;
    private MemberEntity member2;
    private CommunityEntity community;

    @BeforeEach
    public void setup() {

        member1 = MemberEntity.create("hj", "hhj", "hj1234!", "01011111111", "001", "1234123411111");
        member2 = MemberEntity.create("hj2", "hhj2", "hj21234!", "01011111112", "002", "1234123411222");
        memberRepository.save(member1);
        memberRepository.save(member2);

        community = CommunityEntity.create(
                member1.getId(),
                "맛집탐방",
                "1468152645150",
                3257500L,
                200000L,
                10L
        );
        communityRepository.save(community);

        MembershipEntity m1 = MembershipEntity.create( member1, community);
        MembershipEntity m2 = MembershipEntity.create( member2, community);
        membershipRepository.saveAll(List.of(m1, m2));
    }

    @Test
    public void testGetPlanReceipt_Success() {
        PlanEntity plan = PlanEntity.create(
                community.getId(),
                "수리고등학교 동창 강릉 여행",
                LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 23, 59),
                "맛집 투어",
                List.of("스타벅스 성수역점", "동화가든", "카페 툇마루"),
                List.of(member1.getId(), member2.getId())
        );
        planRepository.save(plan);

        // When
        GetReceiptResponseDto response = planService.getPlanReceipt(plan.getId());

        // Then
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getRecords()).hasSize(12);
        Assertions.assertThat(response.getTotalAmt()).isEqualTo(594670);
        Assertions.assertThat(response.getPerAmt()).isEqualTo(297335);
    }

    @Test
    public void testGetPlanReceipt_PlanNotFound() {
        // Given
        UUID nonExistentPlanId = UUID.randomUUID();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            planService.getPlanReceipt(nonExistentPlanId);
        });
    }

    @Test
    public void testGetPlanReceipt_CommunityNotFound() {
        // Given
        PlanEntity plan = PlanEntity.create(
                UUID.randomUUID(),
                "수리고등학교 동창 강릉 여행",
                LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 23, 59),
                "맛집 투어",
                List.of("스타벅스 성수역점", "동화가든", "카페 툇마루"),
                List.of(member1.getId(), member2.getId())
        );
        planRepository.save(plan);

        // When & Then
        assertThrows(CommunityNotFoundException.class, () -> {
            planService.getPlanReceipt(plan.getId());
        });
    }
}