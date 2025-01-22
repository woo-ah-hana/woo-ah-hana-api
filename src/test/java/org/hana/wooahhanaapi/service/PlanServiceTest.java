package org.hana.wooahhanaapi.service;

import jakarta.transaction.Transactional;
import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.entity.MembershipEntity;
import org.hana.wooahhanaapi.domain.community.exception.CommunityNotFoundException;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.community.repository.MembershipRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.*;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.hana.wooahhanaapi.domain.plan.service.PostService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class PlanServiceTest {
    @Autowired
    private PlanService planService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MembershipRepository membershipRepository;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;

    private MemberEntity member1;
    private MemberEntity member2;
    private CommunityEntity community;

    @BeforeAll
    public void setUp() {
        member1 = MemberEntity.create("01011111111", "함형주", "hj1234!", "01011111111", "001", "1234123411111");
        member2 = MemberEntity.create("01011111112", "김미강", "hj21234!", "01011111112", "002", "1234123411222");
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

        // plan
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.create(
                community.getId(),
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
    void getMembers() {
        // given
        List<GetMembersResponseDto> memberNames = planService.getMembers(community.getId());
        // then
        assertNotNull(memberNames);
        assertEquals(2, memberNames.size());
        assertEquals(memberNames.get(0).getName(),"함형주");
        assertEquals(memberNames.get(1).getName(),"김미강");
    }

    @Test
    void getPlanById() {
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.create(
                UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "테스트플랜",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "aaa",
                locations,
                memberIds
        );
        planRepository.save(planEntity);
        // when
        List<Plan> plan = planService.getPlans(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        // then
        assertEquals(1, plan.size());
        assertEquals("aaa", plan.get(0).getCategory());
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
    public void deletePlan() throws IOException {
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();

        CreatePlanRequestDto requestDto = CreatePlanRequestDto.builder()
                .communityId(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"))
                .title("테스트 플랜")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .locations(locations)
                .category("shopping")
                .memberIds(memberIds)
                .build();
        UUID createPlanId = planService.createPlan(requestDto);

        String description = "Post description";
        CreatePostRequestDto requestDto2 = CreatePostRequestDto.builder()
                .planId(createPlanId)
                .memberId(memberRepository.findByUsername("01011111111").get().getId())
                .description(description)
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "image content".getBytes());
        UUID postId = postService.createPost(requestDto2, image);

        // When
        planService.deletePlan(createPlanId.toString());

        // Then
        // 계획 삭제했을 때 추억들도 함께 삭제되어야 함(cascade하게)
        PlanEntity foundPlan = planRepository.findById(createPlanId).orElse(null);
        Assertions.assertNull(foundPlan);
        List<PostEntity> foundPost = postRepository.findCompletedByPlanId(createPlanId);
        Assertions.assertEquals(0, foundPost.size());

    }

    @Test
    void getPlans() {
        // given
        List<String> locations = new ArrayList<>();
        List<UUID> memberIds = new ArrayList<>();
        PlanEntity planEntity = PlanEntity.create(
                community.getId(),
                "테스트플랜2",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "shopping",
                locations,
                memberIds
        );
        planRepository.save(planEntity);
        // when
        List<Plan> plan = planService.getPlans(community.getId());
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
        PlanEntity planEntity = PlanEntity.create(
                UUID.fromString("6fa85f64-5717-4562-b3fc-2c963f66afa6"),
                "테스트플랜3",
                startDate,
                endDate,
                "shopping",
                locations,
                memberIds
        );

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
        PlanEntity plan = PlanEntity.create(
                community.getId(),
                "수리고등학교 동창 강릉 여행",
                LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 23, 59),
                "맛집 투어",
                List.of("스타벅스 성수역점", "동화가든", "카페 툇마루"),
                List.of(member1.getId(), member2.getId())
        );
        PlanEntity planId = planRepository.save(plan);
        UpdatePlanRequestDto updateRequest = UpdatePlanRequestDto.builder()
                .title("업데이트된 플랜")
                .category("meeting")
                .build();
        // when
        planService.updatePlan(planId.getId(), updateRequest);
        // then
        Optional<PlanEntity> updatedPlan = planRepository.findById(planId.getId());
        assertEquals("업데이트된 플랜", updatedPlan.get().getTitle());
        assertEquals("meeting", updatedPlan.get().getCategory());
    }

    @Test
    void EntityNotFoundException() {
        // given
        UUID planId = UUID.randomUUID();
        // when
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> planService.deletePlan(planId.toString())
        );
        // then
        assertTrue(exception.getMessage().contains("해당 plan을 찾을 수 없습니다."));
    }

//    @Test
//    public void testGetPlanReceipt_Success() {
//        PlanEntity plan = PlanEntity.create(
//                community.getId(),
//                "수리고등학교 동창 강릉 여행",
//                LocalDateTime.of(2025, 1, 10, 0, 0),
//                LocalDateTime.of(2025, 1, 12, 23, 59),
//                "맛집 투어",
//                List.of("스타벅스 성수역점", "동화가든", "카페 툇마루"),
//                List.of(member1.getId(), member2.getId())
//        );
//        PlanEntity planEntity = planRepository.save(plan);
//        // When
//        GetReceiptResponseDto response = planService.getPlanReceipt(planEntity.getId());
//        // Then
//        assertThat(response).isNotNull();
//        assertThat(response.getRecords()).hasSize(12);
//        assertThat(response.getTotalAmt()).isEqualTo(594670);
//        assertThat(response.getPerAmt()).isEqualTo(297335);
//    }

    @Test
    public void testGetPlanReceipt_PlanNotFound() {
        // Given
        UUID nonExistentPlanId = UUID.randomUUID(); //임의의 plan Id
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            planService.getPlanReceipt(nonExistentPlanId);
        });
    }

    @Test
    public void testGetPlanReceipt_CommunityNotFound() {
        // Given
        PlanEntity plan = PlanEntity.create(
                UUID.randomUUID(), //임의의 community ID
                "수리고등학교 동창 강릉 여행",
                LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 23, 59),
                "맛집 투어",
                List.of("스타벅스 성수역점", "동화가든", "카페 툇마루"),
                List.of(member1.getId(), member2.getId())
        );
        PlanEntity planEntity = planRepository.save(plan);
        // When & Then
        assertThrows(CommunityNotFoundException.class, () -> {
            planService.getPlanReceipt(planEntity.getId());
        });
    }
}