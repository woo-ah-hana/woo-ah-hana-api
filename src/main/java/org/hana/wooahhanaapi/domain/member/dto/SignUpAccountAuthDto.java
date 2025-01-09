package org.hana.wooahhanaapi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpAccountAuthDto {

    String name;
    String accountNumber;
    String accountBank;
}
