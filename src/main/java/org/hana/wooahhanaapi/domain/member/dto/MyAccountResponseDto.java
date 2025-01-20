package org.hana.wooahhanaapi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyAccountResponseDto {
    private String bankTranId;
    private String accountNumber;
    private String name;
    private Long amount;
}