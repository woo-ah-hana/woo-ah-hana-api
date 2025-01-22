package org.hana.wooahhanaapi.domain.community.dto;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterInCommunityRequestDto {
    private UUID memberId;
    private UUID communityId;
}
