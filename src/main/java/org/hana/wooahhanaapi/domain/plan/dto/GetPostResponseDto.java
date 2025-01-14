package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class GetPostResponseDto {
    private UUID id;
    private UUID memberId;
    private String imageLink;
    private String description;
    private LocalDateTime createdAt;

}
