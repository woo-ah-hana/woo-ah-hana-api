package org.hana.wooahhanaapi.domain.community.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommunityDepositInfoRespDto {

    private String bankTranId;
    private String memberAccountNumber;
    private Long memberAccountBalance ;
    private String communityAccountBank;
    private String communityAccountNumber;
}
