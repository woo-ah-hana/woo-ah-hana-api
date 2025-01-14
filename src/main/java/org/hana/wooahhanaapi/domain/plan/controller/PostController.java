package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePostResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPostResponseDto;
import org.hana.wooahhanaapi.domain.plan.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CreatePostResponseDto createPost(
            @RequestPart("data") CreatePostRequestDto requestDto,
            @RequestPart("image") MultipartFile image
    ) throws IOException {
        return this.postService.createPost(requestDto, image);
    }

    @DeleteMapping("/{postId}")
    public String deletePost(@PathVariable String postId) {
        postService.deletePost(postId);
        return "Post가 성공적으로 삭제되었습니다.";
    }
    @GetMapping("/completed/{planId}")
    public List<GetPostResponseDto> getPostsByPlanId(@PathVariable UUID planId) {
        return postService.getPostsByPlanId(planId);
    }
}
