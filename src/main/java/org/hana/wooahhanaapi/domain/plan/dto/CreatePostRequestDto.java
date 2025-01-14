package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequestDto {
    private String planId;
    private String memberId;
    private String description;
    private LocalDateTime createAt;
}