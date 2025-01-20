package org.hana.wooahhanaapi.service;
import jakarta.transaction.Transactional;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.UpdatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class PlanServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;

    @BeforeAll
    public void setUp() {
        // member
        MemberEntity memberEntity = MemberEntity.create(
                "010-7767-3813",
                "윤영헌",
                "1234",
                "010-7767-3813",
                "1111111111111",
                "001"
        );
        memberRepository.save(memberEntity);

        // plan
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        String communityId = "4fa85f64-5717-4562-b3fc-2c963f66afa6";
        PlanEntity planEntity = PlanEntity.create(
                UUID.randomUUID(), //id
                UUID.fromString(communityId), // communityId
                "성수나들이",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "shopping",
                locations,
                memberIds
        );
        planRepository.save(planEntity);
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
        assertEquals(1, plan.size());
        assertEquals("aaa",plan.get(0).getCategory());
    }

    @Test
    void createPlan() {
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        CreatePlanRequestDto requestDto = CreatePlanRequestDto.builder()
                .communityId(UUID.fromString("5fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .title("테스트플랜")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .locations(locations)
                .category("aaa")
                .memberIds(memberIds)
                .build();
        // when
        UUID planId = planService.createPlan(requestDto);
        UUID CommunityId = requestDto.getCommunityId();
        List<PlanEntity> savedPlan = planRepository.findAllByCommunityId(CommunityId);
        PlanEntity savedEntity = savedPlan.get(0);
        // then
        assertNotNull(planId);
        assertEquals("테스트플랜", savedEntity.getTitle());
    }

    @Test
    void deletePlan() {
        // given
        List<PlanEntity> savedPlans = planRepository.findAll();
        UUID planId = savedPlans.get(0).getId();
        // when
        planService.deletePlan(String.valueOf(planId));
        // then
        Optional<PlanEntity> deletedEntity = planRepository.findById(planId);
        assertTrue(deletedEntity.isEmpty());
    }

    @Test
    void getPlans() {
        // given
        UUID communityId = UUID.fromString("4fa85f64-5717-4562-b3fc-2c963f66afa6");
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.builder()
                .communityId(communityId)
                .title("테스트플랜2")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .locations(locations)
                .category("shopping")
                .memberIds(memberIds)
                .build();
        planRepository.save(planEntity);
        // when
        List<Plan> plan = planService.getPlans(communityId);
        // then
        assertEquals(2, plan.size());
        assertEquals("성수나들이", plan.get(0).getTitle());
        assertEquals("테스트플랜2", plan.get(1).getTitle());
    }

    @Test
    void getCompletedPlans() {
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 2, 18, 0);
        PlanEntity planEntity = PlanEntity.builder()
                .communityId(UUID.fromString("6fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .title("테스트플랜3")
                .startDate(startDate)
                .endDate(endDate)
                .locations(locations)
                .category("shopping")
                .memberIds(memberIds)
                .build();
        planRepository.save(planEntity);
        // when
        UUID communityId = UUID.fromString("6fa85f64-5717-4562-b3fc-2c963f66afa6");
        List<Plan> plans = planService.getCompletedPlans(communityId);
        // then
        assertEquals(1, plans.size());
        assertEquals(startDate, plans.get(0).getStartDate());
        assertEquals(endDate, plans.get(0).getEndDate());
        assertTrue(plans.get(0).getEndDate().isBefore(LocalDateTime.now()));
    }

    @Test
    void updatePlan() {
        // given
        List<PlanEntity> savedPlans = planRepository.findAll();
        UUID planId = savedPlans.get(0).getId();
        UpdatePlanRequestDto updateRequest = UpdatePlanRequestDto.builder()
                .title("업데이트된 플랜")
                .category("meeting")
                .build();
        // when
        planService.updatePlan(planId, updateRequest);
        // then
        Optional<PlanEntity> updatedPlan = planRepository.findById(planId);
        assertEquals("업데이트된 플랜",updatedPlan.get().getTitle());
        assertEquals("meeting",updatedPlan.get().getCategory());
    }
}