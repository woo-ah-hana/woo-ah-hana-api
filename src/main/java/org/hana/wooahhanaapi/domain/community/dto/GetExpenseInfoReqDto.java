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
public class GetExpenseInfoReqDto {
    UUID communityId;
    String fromDate;
    String toDate;
}
