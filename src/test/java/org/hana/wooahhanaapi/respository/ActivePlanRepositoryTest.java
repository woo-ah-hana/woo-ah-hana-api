package org.hana.wooahhanaapi.respository;

import org.hana.wooahhanaapi.domain.activePlan.entity.ActivePlanEntity;
import org.hana.wooahhanaapi.domain.activePlan.repository.ActivePlanRepository;
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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public class ActivePlanRepositoryTest {
    @Autowired
    private ActivePlanRepository activePlanRepository;
    @Autowired
    private PlanRepository planRepository;

    @BeforeAll
    public void setUp() {
        ActivePlanEntity activeplanEntity = ActivePlanEntity.create(
                UUID.randomUUID(), // id
                UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"), //planId
                "2025-01-17",
                "동화가든에서 아침 식사",
                "14:00",
                "강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.",
                "강원특별자치도 강릉시 초당순두부길77번길 15 동화가든",
                "https://www.donghwagarden.com/",
                "1289146373",
                "377911797"
        );
        activePlanRepository.save(activeplanEntity);
    }

    @Test
    @DisplayName("ActivePlan 생성")
    @Transactional
    public void createActivePlan() {
        // given
        long count = activePlanRepository.count();
        ActivePlanEntity activePlanEntity = ActivePlanEntity.builder()
                .planId(UUID.randomUUID())
                .date("2025-01-17")
                .schedule("동화가든에서 아침 식사")
                .time("14:00")
                .description("강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.")
                .address("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든")
                .link("https://www.donghwagarden.com/")
                .mapx("1289146373")
                .mapy("377911797")
                .build();
        // when
        activePlanRepository.save(activePlanEntity);
        long updatedCount = activePlanRepository.count();
        // then
        assertEquals(count + 1, updatedCount);
        assertEquals("2025-01-17", activePlanEntity.getDate());
        assertEquals("동화가든에서 아침 식사",activePlanEntity.getSchedule());
    }

    @Test
    @DisplayName("ActivePlan 읽기")
    @Transactional
    public void readPlan() {
        // when
        List<ActivePlanEntity> plan = activePlanRepository.findAll();
        // then
        assertThat(plan).isNotEmpty();
        assertEquals("동화가든에서 아침 식사", plan.get(0).getSchedule());
    }

    @Test
    @DisplayName("ActivePlan 삭제")
    @Transactional
    public void deletePlan() {
        //Given
        List<ActivePlanEntity> activePlans = activePlanRepository.findAll();
        ActivePlanEntity plan = activePlans.get(0);
        UUID planId = plan.getId();
        // when
        activePlanRepository.delete(plan);
        long countAfterDelete = activePlanRepository.count();
        // then
        ActivePlanEntity deletePlan = activePlanRepository.findById(planId).orElse(null);
        assertThat(deletePlan).isNull();
        assertEquals(activePlans.size() - 1, countAfterDelete);
        assertDoesNotThrow(() -> activePlanRepository.delete(plan));
    }

    @Test
    @DisplayName("planId로 activePlan 불러오기")
    @Transactional
    public void findByPlanId() {
        // given
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "meeting",
                List.of("서울", "부산"),
                memberIds
        ));
        ActivePlanEntity activePlanEntity = ActivePlanEntity.builder()
                .planId(plan.getId()) // planId 추가
                .date("2025-01-17")
                .schedule("동화가든에서 점심 식사")
                .time("14:00")
                .description("강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.")
                .address("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든")
                .link("https://www.donghwagarden.com/")
                .mapx("1289146373")
                .mapy("377911797")
                .build();
        activePlanRepository.save(activePlanEntity);
        // when
        List<ActivePlanEntity> activePlan = activePlanRepository.findByPlanId(plan.getId());
        // then
        assertEquals("동화가든에서 점심 식사", activePlan.get(0).getSchedule());
        List<ActivePlanEntity> emptyResult = activePlanRepository.findByPlanId(UUID.randomUUID());
        assertThat(emptyResult).isEmpty();
    }
}
