package org.hana.wooahhanaapi.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String accountNumber;
    private String bankTranId;
}
