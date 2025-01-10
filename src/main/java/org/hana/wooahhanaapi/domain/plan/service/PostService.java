package org.hana.wooahhanaapi.domain.plan.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.member.repository.MemberRepository;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostResponseDto;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.entity.PostEntity;
import org.hana.wooahhanaapi.domain.plan.exception.*;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.hana.wooahhanaapi.domain.plan.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final S3Service s3Service;
    private final PlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public CreatePostResponseDto createPost(CreatePostRequestDto requestDto, MultipartFile image) throws IOException {

        // 값이 들어있는지 확인
        if (requestDto.getPlanId() == null || requestDto.getMemberId() == null || requestDto.getDescription() == null
                ||image == null || image.isEmpty()) {
            throw new InvalidPostDataException("필수 데이터가 누락되었습니다.");
        }

        // 이미지 파일 형식 확인
        if (!Objects.requireNonNull(image.getContentType()).startsWith("image/")) {
            throw new InvalidFileTypeException("지원되지 않는 파일 형식입니다.");
        }

        // 파일 크기 10MB 제한
        if (image.getSize() > 10 * 1024 * 1024) {
            throw new FileSizeExceededException("업로드 가능한 파일 크기를 초과했습니다.");
        }

        PlanEntity plan = planRepository.findById(UUID.fromString(requestDto.getPlanId()))
                .orElseThrow(() -> new EntityNotFoundException("해당 Plan이 존재하지 않습니다."));

        MemberEntity member = memberRepository.findById(UUID.fromString(requestDto.getMemberId()))
                .orElseThrow(() -> new EntityNotFoundException("해당 Member가 존재하지 않습니다."));

        String s3FileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
        /* 임시로 S3 url 대신 fileName으로 db 저장" */
//        String imageUrl = s3Service.upload(image, s3FileName);
        String imageUrl = s3FileName;

        PostEntity post = PostEntity.builder()
                .plan(plan)
                .member(member)
                .imageUrl(imageUrl)
                .description(requestDto.getDescription())
                .build();
        postRepository.save(post);

        return CreatePostResponseDto.builder()
                .postId(post.getId().toString())
                .imageUrl(post.getImageUrl())
                .description(post.getDescription())
                .build();
    }

    @Transactional
    public void deletePost(String postId) {
        PostEntity post = postRepository.findById(UUID.fromString(postId))
                .orElseThrow(() -> new EntityNotFoundException("해당 Post를 찾을 수 없습니다."));

        String fileUrl = post.getImageUrl();
//        s3Service.delete(fileUrl);

        postRepository.delete(post);
    }
}