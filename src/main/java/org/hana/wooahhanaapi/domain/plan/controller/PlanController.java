package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.dto.MemberResponseDto;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.ListPlanResponseDto;
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

    @GetMapping("/list/{communityId}")
    public List<ListPlanResponseDto> listPlans(@PathVariable UUID communityId) {
        return planService.getPlanList(communityId);
    }
}
