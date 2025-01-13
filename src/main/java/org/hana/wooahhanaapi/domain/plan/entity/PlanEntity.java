package org.hana.wooahhanaapi.domain.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.utils.annotations.ListConverter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column(nullable = false, name="community_id")
    private UUID communityId;

    @Column(nullable=false, name = "title")
    private String title;

    @Column(nullable=false, name = "start_date")
    private LocalDateTime startDate;

    @Column(nullable=false, name = "end_date")
    private LocalDateTime endDate;

    @Column(nullable=false, name = "category")
    protected String category;

    @Column(name = "locations")
    @Convert(converter = ListConverter.class)
    protected List<String> locations;

    @Column(name = "member_ids")
    @Convert(converter = ListConverter.class)
    private List<UUID> memberIds;

    public void update(String title, LocalDateTime startDate, LocalDateTime endDate, String category, List<String> locations, List<UUID> memberIds) {
        if (title != null) this.title = title;
        if (startDate != null) this.startDate = startDate;
        if (endDate != null) this.endDate = endDate;
        if (category != null) this.category = category;
        if (locations != null) this.locations = locations;
        if (memberIds != null) this.memberIds = memberIds;
    }

}
