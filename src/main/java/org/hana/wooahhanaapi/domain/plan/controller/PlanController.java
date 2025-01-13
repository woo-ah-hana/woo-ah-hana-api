package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping("/create")
    public UUID createPlan(@RequestBody CreatePlanRequestDto requestDto) {
        return planService.createPlan(requestDto);
    }

    @DeleteMapping("/{planId}")
    public String deletePlan(@PathVariable String planId){
        planService.deletePlan(planId);
        return "Plan이 성공적으로 삭제되었습니다.";
    }
}
