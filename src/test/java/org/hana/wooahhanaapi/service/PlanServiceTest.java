package org.hana.wooahhanaapi.service;

import org.hana.wooahhanaapi.domain.community.entity.CommunityEntity;
import org.hana.wooahhanaapi.domain.community.repository.CommunityRepository;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.hana.wooahhanaapi.domain.plan.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TestPropertySource(locations = "classpath:application-test.yml")
@SpringBootTest
@ActiveProfiles("test")
public class PlanServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlanService planService;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private CommunityRepository communityRepository;
    @Autowired
    private PostRepository postRepository;

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
    @Test
    void createPlan(){
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
        // when
        UUID createPlanId = planService.createPlan(requestDto);

        // then
        Assertions.assertNotNull(createPlanId);

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
                .memberId(memberRepository.findByUsername("010-7767-3813").get().getId())
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
}
