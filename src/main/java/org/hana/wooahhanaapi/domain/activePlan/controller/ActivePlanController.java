package org.hana.wooahhanaapi.domain.activePlan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.activePlan.domain.ActivePlan;
import org.hana.wooahhanaapi.domain.activePlan.dto.CreateActivePlanRequestDto;
import org.hana.wooahhanaapi.domain.activePlan.service.ActivePlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/activePlan")
@RequiredArgsConstructor

public class ActivePlanController {

    private final ActivePlanService activePlanService;

    @PostMapping("/create")
    public UUID createActivePlan(@RequestBody CreateActivePlanRequestDto requestDto) {
        return activePlanService.createActivePlan(requestDto);
    }
}
