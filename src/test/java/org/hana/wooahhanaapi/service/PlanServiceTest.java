package org.hana.wooahhanaapi.service;

import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
public class PlanServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;

    @BeforeEach
    void seed() {
        MemberEntity memberEntity = MemberEntity.create(
                "010-7767-3813",
                "윤영헌",
                "1234",
                "010-7767-3813",
                "1111111111111",
                "001"
        );

        memberRepository.save(memberEntity);
    }

    @Test
    void getPlanById() {
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.builder()
                .communityId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .title("테스트플랜")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .locations(locations)
                .category("aaa")
                .memberIds(memberIds)
                .build();
        planRepository.save(planEntity);

        // when
        List<Plan> plan = planService.getPlans(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));

        // then
        Assertions.assertEquals(1, plan.size());
    }
}
