package org.hana.wooahhanaapi.domain.plan.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.exception.LogicalPlanDataException;
import org.hana.wooahhanaapi.utils.annotations.ListConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Plan {
    private UUID id;

    private UUID communityId;

    private String title;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String category;

    private List<String> locations;

    private List<UUID> memberIds;

    public static Plan create(UUID id, UUID communityId, String title, LocalDateTime startDate, LocalDateTime endDate, String category, List<String> locations, List<UUID> memberIds) {
        if(startDate.isAfter(endDate)) {throw new LogicalPlanDataException("종료일은 시작일 이후여야 합니다.");}
        return new Plan( id, communityId, title, startDate, endDate, category, locations, memberIds );
    }
}

