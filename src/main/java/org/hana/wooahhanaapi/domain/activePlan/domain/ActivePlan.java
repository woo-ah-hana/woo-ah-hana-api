package org.hana.wooahhanaapi.domain.activePlan.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivePlan {
    private UUID id;
    private UUID planId;
    private String date;
    private String schedule;
    private String time;
    private String description;
    private String address;
    private String link;
    private String mapx;
    private String mapy;
    public static ActivePlan create(UUID id,UUID planId, String date,String schedule, String time, String description, String address, String link, String mapx, String mapy) {
        return new ActivePlan( id, planId,date, schedule, time, description, address, link, mapx ,mapy);
    }

}

