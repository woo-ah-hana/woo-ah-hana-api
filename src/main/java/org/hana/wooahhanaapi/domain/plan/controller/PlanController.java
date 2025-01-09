package org.hana.wooahhanaapi.domain.plan.controller;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.dto.PlanRequestDto;
import org.hana.wooahhanaapi.domain.plan.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @PostMapping("/create")
    public ResponseEntity<PlanRequestDto> createPlan(@RequestBody PlanRequestDto requestDto) {
        planService.createPlan(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestDto);
    }
}
