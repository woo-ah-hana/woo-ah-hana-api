package org.hana.wooahhanaapi.domain.community.adapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendValidationCodeReqDto {
    private String bankTranId; //하나은행:"001" 1.계좌 생성 시 무조건 "001" 2.회원가입 시 은행 선택
    private String accountNumber;
}
