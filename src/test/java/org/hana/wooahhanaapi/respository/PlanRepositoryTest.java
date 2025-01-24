package org.hana.wooahhanaapi.respository;

import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeAll
    public void setUp() {
        MemberEntity member = memberRepository.save(MemberEntity.create("01045837166", "최선정", "password", "01045837166", "3561057204498", "001"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.fromString("ed81955e-7fb2-41cd-bab1-e3f6da7ed5b9"),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "meeting",
                List.of("서울", "부산"),
                List.of(member.getId())
        ));
    }

    @Test
    @DisplayName("plan 생성")
    @Transactional
    public void createPlan() {
        // given
        long count = planRepository.count();
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.builder()
                .communityId(UUID.fromString("5fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .title("테스트플랜")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .locations(locations)
                .category("aaa")
                .memberIds(memberIds)
                .build();
        // when
        planRepository.save(planEntity);
        long updatedCount = planRepository.count();
        // then
        assertEquals(count + 1, updatedCount);
        assertEquals("테스트플랜", planEntity.getTitle());
    }

    @Test
    @DisplayName("plan 읽기")
    @Transactional
    public void readPlan() {
        List<PlanEntity> plan = planRepository.findAll();
        assertEquals("Plan Title", plan.get(0).getTitle());
    }

    @Test
    @DisplayName("plan 삭제")
    @Transactional
    public void deletePlan() {
        //Given
        PlanEntity plan = planRepository.findAll().get(0);
        UUID planId = plan.getId();
        // when
        planRepository.delete(plan);
        // then
        PlanEntity deletePlan = planRepository.findById(planId).orElse(null);
        assertThat(deletePlan).isNull();
    }
    
    @Test
    @DisplayName("완료된 plan 불러오기")
    @Transactional
    public void findCompletedByCommunityId(){
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 1, 1, 18, 0);  // 2025.01.01 18:00:00
        PlanEntity planEntity = PlanEntity.create(
                UUID.randomUUID(),
                "테스트 플랜",
                startDate,
                endDate,
                "shopping",
                locations,
                memberIds
        );
        // when
        planRepository.save(planEntity);
        List<PlanEntity> plan = planRepository.findCompletedByCommunityId(planEntity.getCommunityId());
        // then
        assertEquals("shopping",plan.get(0).getCategory());
    }

    @Test
    @DisplayName("진행중인 plan 불러오기")
    @Transactional
    public void findAllByCommunityId(){
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2025, 1, 1, 10, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 2, 1, 18, 0);  // 2025.02.01 18:00:00
        PlanEntity planEntity = PlanEntity.create(
                UUID.fromString("ed81955e-7fb2-41cd-bab1-e3f6da7ed5b9"),
                "테스트 플랜",
                startDate,
                endDate,
                "shopping",
                locations,
                memberIds
        );
        // when
        planRepository.save(planEntity);
        List<PlanEntity> plan = planRepository.findAllByCommunityId(planEntity.getCommunityId());
        // then
        assertEquals("shopping",plan.get(1).getCategory());
        assertEquals(2, plan.size());
    }
}
