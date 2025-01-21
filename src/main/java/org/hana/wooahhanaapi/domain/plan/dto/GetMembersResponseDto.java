package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class GetMembersResponseDto {
    private UUID id;
    private String name;
}
