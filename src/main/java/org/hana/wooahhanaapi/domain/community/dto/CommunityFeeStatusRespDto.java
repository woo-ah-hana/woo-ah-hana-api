package org.hana.wooahhanaapi.domain.community.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityFeeStatusRespDto {

    private Set<CommunityFeeStatusRespListDto> paidMembers;
    private Set<CommunityFeeStatusRespListDto> unpaidMembers;

}
