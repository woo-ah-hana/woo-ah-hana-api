package org.hana.wooahhanaapi.domain.community.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExpensePlanInfoDto {
    private String title;
    private String category;

}
