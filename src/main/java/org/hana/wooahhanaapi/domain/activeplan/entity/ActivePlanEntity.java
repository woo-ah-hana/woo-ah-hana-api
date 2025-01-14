package org.hana.wooahhanaapi.domain.activeplan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.entity.MockPlanEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "active_plan")
@Entity
public class ActivePlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private MockPlanEntity plan;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 255)
    private String schedule;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String address;

    @Column(length = 500)
    private String link;

    private Long mapx;

    private Long mapy;
}
