package org.hana.wooahhanaapi.domain.community.dto;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterInCommunityResponseDto {
    private String memberName;
    private String communityName;
    private boolean success;
}
