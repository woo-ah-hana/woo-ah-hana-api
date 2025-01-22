package org.hana.wooahhanaapi.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityFullInfoResponseDto {

    private UUID id;
    private UUID managerId;
    private String name;
    private String accountNumber;
    private Long credits;
    private Long fee;
    private Long feePeriod;
}
