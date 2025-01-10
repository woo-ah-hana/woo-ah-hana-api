package org.hana.wooahhanaapi.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCreateReqDto {

    @NotBlank
    private UUID managerId;
    @NotBlank
    private String name;
    @NotBlank
    private String accountNumber;
    @NotBlank
    private Long credits;
    @NotBlank
    private Long fee;
    @NotBlank
    private Long feePeriod;
}
