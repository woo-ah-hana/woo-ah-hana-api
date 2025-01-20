package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.CreatePlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.dto.GetPlansResponseDto;

import org.hana.wooahhanaapi.domain.plan.dto.GetReceiptResponseDto;
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

    // 계획 생성 폼 제출
    @PostMapping("/create")
    public UUID createPlan(@RequestBody CreatePlanRequestDto requestDto) {
        return planService.createPlan(requestDto);
    }

    // 계획 생성 버튼 누른 후 프론트엔드에 모임의 멤버 목록을 전송
    @GetMapping("/createInfo/{communityId}")
    public List<String> getCommunityMembers(@PathVariable UUID communityId) {
        return planService.getMembers(communityId);
    }

    @DeleteMapping("/{planId}")
    public String deletePlan(@PathVariable String planId){
        planService.deletePlan(planId);
        return "Plan이 성공적으로 삭제되었습니다.";
    }

    @GetMapping("/{planId}")
    public GetPlansResponseDto getPlan(@PathVariable UUID planId) {
        return planService.getPlanDetail(planId);
    }

    @GetMapping("/list/{communityId}")
    public List<Plan> getPlans(@PathVariable UUID communityId) {
        return planService.getPlans(communityId);
    }

    @PatchMapping("/update/{planId}")
    public String updatePlan(@PathVariable UUID planId, @RequestBody UpdatePlanRequestDto requestDto) {
        planService.updatePlan(planId, requestDto);
        return "Plan이 성공적으로 업데이트 되었습니다.";
    }

    @GetMapping("/completed/{communityId}")
    public List<Plan> getCompletedPlans(@PathVariable UUID communityId) {
        return planService.getCompletedPlans(communityId);
    }

    @GetMapping("/receipt/{planId}")
    public GetReceiptResponseDto getPlanReceipt(@PathVariable UUID planId) {
        return planService.getPlanReceipt(planId);
    }

}