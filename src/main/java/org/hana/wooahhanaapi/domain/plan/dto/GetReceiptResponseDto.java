package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hana.wooahhanaapi.domain.community.dto.CommunityTrsfRecordRespDto;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class GetReceiptResponseDto {
    private List<CommunityTrsfRecordRespDto> records;
    private Long totalAmt;
    private Long perAmt;
}
