package org.hana.wooahhanaapi.service;

import jakarta.transaction.Transactional;
import org.hana.wooahhanaapi.domain.activePlan.domain.ActivePlan;
import org.hana.wooahhanaapi.domain.activePlan.dto.CreateActivePlanRequestDto;
import org.hana.wooahhanaapi.domain.activePlan.entity.ActivePlanEntity;
import org.hana.wooahhanaapi.domain.activePlan.repository.ActivePlanRepository;
import org.hana.wooahhanaapi.domain.activePlan.service.ActivePlanService;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ActivePlanServiceTest {
    @Autowired
    private ActivePlanService activePlanService;
    @Autowired
    private ActivePlanRepository activePlanRepository;

    @BeforeAll
    public void setUp(){
        ActivePlanEntity activeplanEntity = ActivePlanEntity.create(
                UUID.randomUUID(),
                UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
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
    void saveActivePlans() {
        // given
        List<CreateActivePlanRequestDto> requestDtos = new ArrayList<>();
        CreateActivePlanRequestDto requestDto1 = CreateActivePlanRequestDto.builder()
                .planId(UUID.randomUUID())
                .date("2025-01-18")
                .schedule("동화가든에서 점심 식사")
                .time("14:00")
                .description("강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.")
                .address("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든")
                .link("https://www.donghwagarden.com/")
                .mapx("1289146373")
                .mapy("377911797")
                .build();
        CreateActivePlanRequestDto requestDto2 = CreateActivePlanRequestDto.builder()
                .planId(UUID.randomUUID())
                .date("2025-01-18")
                .schedule("동화가든에서 점심 식사1")
                .time("14:00")
                .description("강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.")
                .address("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든")
                .link("https://www.donghwagarden.com/")
                .mapx("1289146373")
                .mapy("377911797")
                .build();
        requestDtos.add(requestDto1);
        requestDtos.add(requestDto2);

        // when
        List<UUID> planIds = activePlanService.saveActivePlans(requestDtos);

        // then
        assertEquals(2, planIds.size());
    }

    @Test
    void createActivePlan(){
        // given
        CreateActivePlanRequestDto requestDto = CreateActivePlanRequestDto.builder()
                .planId(UUID.randomUUID())
                .date("2025-01-18")
                .schedule("동화가든에서 점심 식사")
                .time("14:00")
                .description("강릉시 초당동에 위치한 '동화가든'은 짬뽕순두부로 유명한 맛집입니다. 아침 식사로 든든하게 시작해보세요.")
                .address("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든")
                .link("https://www.donghwagarden.com/")
                .mapx("1289146373")
                .mapy("377911797")
                .build();
        // when
        UUID activePlanId = activePlanService.createActivePlan(requestDto);
        UUID planId = requestDto.getPlanId();
        List<ActivePlanEntity> savedPlan = activePlanRepository.findByPlanId(planId);
        ActivePlanEntity savedEntity = savedPlan.get(0);
        // then
        assertNotNull(activePlanId);
        assertEquals("2025-01-18",savedEntity.getDate());
        assertEquals("강원특별자치도 강릉시 초당순두부길77번길 15 동화가든",savedEntity.getAddress());
    }

    @Test
    void getActivePlan(){
        // given
        UUID activePlanId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        // when
        List<ActivePlan> activePlan = activePlanService.getActivePlan(activePlanId);
        ActivePlan savedEntity = activePlan.get(0);
        // then
        assertEquals(1, activePlan.size());
        assertEquals("2025-01-17",savedEntity.getDate());
        assertEquals("동화가든에서 아침 식사",savedEntity.getSchedule());
    }

    @Test
    void deleteActivePlan(){
        // given
        UUID activePlanId = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
        // when
        activePlanService.deleteActivePlan(activePlanId);
        // then
        Optional<ActivePlanEntity> deletedEntity = activePlanRepository.findById(activePlanId);
        assertTrue(deletedEntity.isEmpty());
    }

    @Test
    void DeleteEntityNotFoundException() {
        // given
        UUID failedPlanId = UUID.randomUUID();
        // when
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> {
                    activePlanService.deleteActivePlan(failedPlanId);
                });
        // then
        assertTrue(exception.getMessage().contains("해당 plan을 찾을 수 없습니다."));
    }
}