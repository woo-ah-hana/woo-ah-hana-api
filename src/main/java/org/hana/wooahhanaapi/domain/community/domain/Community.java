package org.hana.wooahhanaapi.domain.community.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community {

    private UUID id;
    private UUID managerId;
    private String name;
    private String accountNumber;
    private Long credits;
    private Long fee;
    private Long feePeriod;

    public static Community create(UUID id, UUID managerId, String name, String accountNumber, Long credits, Long fee, Long feePeriod) {
        return new Community(id, managerId, name, accountNumber, credits, fee, feePeriod);
    }

    public void updateFeeInfo(Long fee, Long feePeriod) {
        this.fee = fee;
        this.feePeriod = feePeriod;
    }
}
