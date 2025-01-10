package org.hana.wooahhanaapi.domain.plan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MembershipEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlanEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    @Column(name="id")
    protected UUID id;

    @Column(nullable=false, name = "title")
    protected String title;

    @Column(nullable=false, name = "start_date")
    protected LocalDateTime startDate;

    @Column(nullable=false, name = "end_date")
    protected LocalDateTime endDate;

    @Column(nullable=false, name = "category")
    protected String category;

    @Column(name = "location")
    protected String location;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembershipEntity> memberships; // Plan에 참여한 회원 목록

}
