package org.hana.wooahhanaapi.domain.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class GetPlansResponseDto {
    private UUID id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String category;
    private List<String> locations;
}
