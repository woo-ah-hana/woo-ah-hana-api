package org.hana.wooahhanaapi.domain.community.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityCreateReqDto {

    @NotBlank
    private String name;
    @NotBlank
    private String accountNumber;
    @NotBlank
    private String validationCode;
    @NotBlank
    private Long credits;
    @NotBlank
    private Long fee;
    @NotBlank
    private Long feePeriod;
}
