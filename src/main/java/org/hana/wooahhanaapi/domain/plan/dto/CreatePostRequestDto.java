package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePostRequestDto {
    private UUID planId;
    //private UUID memberId;
    private String description;
    //private LocalDateTime createAt;
}