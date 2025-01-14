package org.hana.wooahhanaapi.domain.plan.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostResponseDto {

    private String postId;
    private String imageUrl;
    private String description;
    private LocalDateTime createAt;

}