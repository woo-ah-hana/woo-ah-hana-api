package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;

import org.hana.wooahhanaapi.domain.plan.dto.UpdatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{planId}")
    public Plan getPlan(@PathVariable UUID planId) {
        return planService.getPlanDetail(planId);
    }

    @GetMapping("/list/{communityId}")
    public List<GetPlansResponseDto> getPlans(@PathVariable UUID communityId) {
        return planService.getPlans(communityId);
    }

    @PatchMapping("/update/{planId}")
    public String updatePlan(@PathVariable UUID planId, @RequestBody UpdatePlanRequestDto requestDto) {
        planService.updatePlan(planId, requestDto);
        return "Plan이 성공적으로 업데이트 되었습니다.";
    }

    @GetMapping("/completed/{communityId}")
    public List<GetPlansResponseDto> getCompletedPlans(@PathVariable UUID communityId) {
        return planService.getCompletedPlans(communityId);
    }
}