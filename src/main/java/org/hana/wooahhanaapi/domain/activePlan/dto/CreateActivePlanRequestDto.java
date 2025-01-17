package org.hana.wooahhanaapi.domain.activePlan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateActivePlanRequestDto {
    private UUID Id;
    private UUID planId;
    private String date;
    private String schedule;
    private String time;
    private String description;
    private String address;
    private String link;
    private String mapx;
    private String mapy;
}
