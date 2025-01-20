package org.hana.wooahhanaapi.respository;

import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.junit.jupiter.api.BeforeAll;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(locations = "classpath:application-test.yml")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@SpringBootTest
public class PostRepositoryTest {

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
    public void setUpPost() {
        postRepository.deleteAll();
        //Given
        PostEntity post = PostEntity.builder()
                .plan(plan)
                .member(member)
                .imageUrl("https://example.com/image.jpg")
                .description("Post description")
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    @Test
    public void testFindCompletedByPlanId() {
        //When
        List<PostEntity> posts = postRepository.findCompletedByPlanId(plan.getId());

        //Then
        assertNotNull(posts);
        assertEquals(1, posts.size());
        assertEquals("Post description", posts.get(0).getDescription());
    }

    @Test
    public void testCreateAndReadPost() {
        // Given
        PostEntity post = PostEntity.builder()
                .plan(plan)
                .member(member)
                .imageUrl("https://example.com/image3.jpg")
                .description("Post description 3")
                .createdAt(LocalDateTime.now())
                .build();

        //When
        postRepository.save(post);

        //Then
        PostEntity foundPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getDescription()).isEqualTo("Post description 3");
    }

    @Test
    public void testDeletePost() {
        //Given
        PostEntity post = postRepository.findAll().get(0);  // 앞서 setUpPost에서 저장된 포스트 가져오기
        UUID postId = post.getId();

        //When
        postRepository.delete(post);

        //Then
        PostEntity deletedPost = postRepository.findById(postId).orElse(null);
        assertThat(deletedPost).isNull();
    }
}
