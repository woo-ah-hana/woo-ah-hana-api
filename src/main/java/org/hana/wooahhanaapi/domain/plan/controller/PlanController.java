package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.member.entity.MemberEntity;
import org.hana.wooahhanaapi.domain.plan.domain.Plan;
import org.hana.wooahhanaapi.domain.plan.dto.*;

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
    public List<GetMembersResponseDto> getCommunityMembers(@PathVariable UUID communityId) {
        return planService.getMembers(communityId);
    }

    // plan 삭제하기
    @DeleteMapping("/{planId}")
    public String deletePlan(@PathVariable String planId){
        planService.deletePlan(planId);
        return "Plan이 성공적으로 삭제되었습니다.";
    }
    // 완료되기전의 plan 상세 정보 가져오기
    @GetMapping("/{planId}")
    public GetPlansResponseDto getPlan(@PathVariable UUID planId) {
        return planService.getPlanDetail(planId);
    }

    // community에 속한 모든 plan 가져오기
    @GetMapping("/list/{communityId}")
    public List<Plan> getPlans(@PathVariable UUID communityId) {
        return planService.getPlans(communityId);
    }

    // plan 정보 업데이트 하기
    @PatchMapping("/update/{planId}")
    public String updatePlan(@PathVariable UUID planId, @RequestBody UpdatePlanRequestDto requestDto) {
        planService.updatePlan(planId, requestDto);
        return "Plan이 성공적으로 업데이트 되었습니다.";
    }

    // 완료된 plan 가져오기
    @GetMapping("/completed/{communityId}")
    public List<Plan> getCompletedPlans(@PathVariable UUID communityId) {
        return planService.getCompletedPlans(communityId);
    }

    // 영수증 조회하기
    @GetMapping("/receipt/{planId}")
    public GetReceiptResponseDto getPlanReceipt(@PathVariable UUID planId) {
        return planService.getPlanReceipt(planId);
    }

}