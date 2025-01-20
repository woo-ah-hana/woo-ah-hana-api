package org.hana.wooahhanaapi.service;

import jakarta.transaction.Transactional;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPostResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.hana.wooahhanaapi.domain.plan.service.PostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private MemberRepository memberRepository;

    private PlanEntity plan;
    private MemberEntity member;

    @BeforeAll
    public void setUp() {
        member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));
    }

    @BeforeEach
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    public void testCreatePost() throws IOException {
        // Given
        String description = "Post description";
        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description(description)
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "image content".getBytes());

        // When
        UUID postId = postService.createPost(requestDto, image);

        // Then
        assertNotNull(postId);
        PostEntity post = postRepository.findById(postId).orElse(null);
        assertNotNull(post);
        assertEquals(description, post.getDescription());
    }

    @Test
    public void testDeletePost() throws IOException {
        // Given
        String description = "Post to delete";
        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description(description)
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile image = new MockMultipartFile("image", "test-image.jpg", "image/jpeg", "image content".getBytes());
        UUID postId = postService.createPost(requestDto, image);

        // When
        postService.deletePost(postId.toString());

        // Then
        PostEntity deletedPost = postRepository.findById(postId).orElse(null);
        assertThat(deletedPost).isNull();
    }

    @Test
    public void testGetPostsByPlanId() throws IOException {
        // Given
        String description1 = "Post 1 description";
        CreatePostRequestDto requestDto1 = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description(description1)
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile image1 = new MockMultipartFile("image", "test-image1.jpg", "image/jpeg", "image content".getBytes());
        postService.createPost(requestDto1, image1);

        String description2 = "Post 2 description";
        CreatePostRequestDto requestDto2 = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description(description2)
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile image2 = new MockMultipartFile("image", "test-image2.jpg", "image/jpeg", "image content".getBytes());
        postService.createPost(requestDto2, image2);

        // When
        List<GetPostResponseDto> posts = postService.getPostsByPlanId(plan.getId());

        // Then
        assertThat(posts).hasSize(2);
        assertThat(posts.get(0).getDescription()).isEqualTo(description1);
        assertThat(posts.get(1).getDescription()).isEqualTo(description2);
    }
}
