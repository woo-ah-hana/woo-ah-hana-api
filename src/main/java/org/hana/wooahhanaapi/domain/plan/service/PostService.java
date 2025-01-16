package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.domain.Post;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPostResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.exception.*;
import org.hana.wooahhanaapi.domain.plan.mapper.PlanMapper;
import org.hana.wooahhanaapi.domain.plan.mapper.PostMapper;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final S3Service s3Service;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public UUID createPost(CreatePostRequestDto requestDto, MultipartFile image) throws IOException {

        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new InvalidFileTypeException("지원되지 않는 파일 형식입니다.");
        }
        if (image.getSize() > 10 * 1024 * 1024) {
            throw new FileSizeExceededException("업로드 가능한 파일 크기를 초과했습니다.");
        }

        String s3FileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        // TODO: S3 서비스에 저장
        String imageUrl = s3FileName;

        PlanEntity plan = planRepository.findById(requestDto.getPlanId())
                .orElseThrow(() -> new EntityNotFoundException("해당 Plan이 존재하지 않습니다."));

        MemberEntity member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("해당 Member가 존재하지 않습니다."));

        Post post = Post.create(
                UUID.randomUUID(),
                plan,
                member,
                imageUrl,
                requestDto.getDescription(),
                requestDto.getCreateAt()
        );

        return  postRepository.save(PostMapper.mapDomainToEntity(post)).getId();
    }

    @Transactional
    public void deletePost(String postId) {
        PostEntity post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new EntityNotFoundException("해당 Post를 찾을 수 없습니다."));

        // TODO: S3 서비스에서 삭제
        postRepository.delete(post);
    }

    public List<GetPostResponseDto> getPostsByPlanId(UUID planId) {
        List<PostEntity> posts = postRepository.findCompletedByPlanId(planId);
        return posts.stream()
                .map(PlanMapper::mapPostsEntityToDto)
                .collect(Collectors.toList());
    }
}
