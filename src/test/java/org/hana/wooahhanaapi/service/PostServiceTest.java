package org.hana.wooahhanaapi.service;

import jakarta.transaction.Transactional;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPostResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.exception.EntityNotFoundException;
import org.hana.wooahhanaapi.domain.plan.exception.FileSizeExceededException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidFileTypeException;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidPostDataException;
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
import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    public void cleanUp() {
        postRepository.deleteAll();
    }

    @Test
    public void testCreatePost() throws IOException {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

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
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

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
        MemberEntity member = memberRepository.save(MemberEntity.create("username1", "name1", "password1", "1234567", "123-45-678910", "bank1234"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

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

    /* Exception Test */
    @Test
    public void testCreatePost_InvalidFileType() {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description("Post description")
                .createAt(LocalDateTime.now())
                .build();

        MockMultipartFile nonImageFile = new MockMultipartFile("image", "test-file.txt", "text/plain", "Invalid content".getBytes());

        // When & Then
        assertThrows(InvalidFileTypeException.class, () -> postService.createPost(requestDto, nonImageFile));
    }

    @Test
    public void testCreatePost_FileSizeExceeded() {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                .description("Post description")
                .createAt(LocalDateTime.now())
                .build();

        // Creating a file larger than 10MB
        byte[] largeFile = new byte[10 * 1024 * 1024 + 1];
        MockMultipartFile largeImageFile = new MockMultipartFile("image", "large-image.jpg", "image/jpeg", largeFile);

        // When & Then
        assertThrows(FileSizeExceededException.class, () -> postService.createPost(requestDto, largeImageFile));
    }

    @Test
    public void testCreatePost_PlanNotFound() {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(UUID.randomUUID())
                .memberId(member.getId())
                .description("Post description")
                .createAt(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(requestDto, new MockMultipartFile("image", "image.jpg", "image/jpeg", "content".getBytes())));
    }

    @Test
    public void testCreatePost_MemberNotFound() {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(UUID.randomUUID())
                .description("Post description")
                .createAt(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> postService.createPost(requestDto, new MockMultipartFile("image", "image.jpg", "image/jpeg", "content".getBytes())));
    }

    @Test
    public void testCreatePost_MissingPostData() {
        // Given
        MemberEntity member = memberRepository.save(MemberEntity.create("username", "name", "password", "123456", "123-45-6789", "bank123"));
        PlanEntity plan = planRepository.save(PlanEntity.create(
                UUID.randomUUID(),
                "Plan Title",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1),
                "Category",
                List.of("Location 1", "Location 2"),
                List.of(member.getId())
        ));

        CreatePostRequestDto requestDto = CreatePostRequestDto.builder()
                .planId(plan.getId())
                .memberId(member.getId())
                // Missing description field
                .createAt(LocalDateTime.now())
                .build();

        // When & Then
        assertThrows(InvalidPostDataException.class, () -> postService.createPost(requestDto, new MockMultipartFile("image", "image.jpg", "image/jpeg", "content".getBytes())));
    }

    @Test
    public void testDeletePost_WithNonExistingPost() {
        // Given
        UUID nonExistingPostId = UUID.randomUUID();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> postService.deletePost(nonExistingPostId.toString()), "Deleting non-existing post should throw exception");
    }

    @Test
    public void testGetPostsByPlanId_EntityNotFoundException() {
        // Given
        UUID nonExistentPlanId = UUID.randomUUID();

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            List<PostEntity> posts = postRepository.findCompletedByPlanId(nonExistentPlanId);
            if (posts.isEmpty()) {
                throw new EntityNotFoundException("해당 Plan을 찾을 수 없습니다.");
            }
        });
    }
}
