package org.hana.wooahhanaapi.domain.activePlan.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "active_plan")
public class ActivePlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;

    @Column( name="plan_id",nullable = false)
    private UUID planId;

    @Column(name="date",nullable = false)
    private String date;

    @Column(name="schedule",nullable = false)
    private String schedule;

    @Column(name="time",nullable = false)
    private String time;

    @Column(name="description",nullable = false)
    private String description;

    @Column(name="address",nullable = false)
    private String address;

    @Column(name="link",nullable = false)
    private String link;

    @Column(name="mapx",nullable = false)
    private String mapx;

    @Column(name="mapy",nullable = false)
    private String mapy;
}
