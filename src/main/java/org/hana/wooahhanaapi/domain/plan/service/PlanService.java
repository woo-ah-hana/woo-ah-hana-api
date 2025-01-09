package org.hana.wooahhanaapi.domain.plan.service;

import lombok.RequiredArgsConstructor;
import org.hana.wooahhanaapi.domain.plan.entity.PlanEntity;
import org.hana.wooahhanaapi.domain.plan.exception.InvalidPlanDataException;

import org.hana.wooahhanaapi.domain.plan.exception.LogicalPlanDataException;
import org.hana.wooahhanaapi.domain.plan.repository.PlanRepository;
import org.springframework.stereotype.Service;
import org.hana.wooahhanaapi.domain.plan.dto.PlanRequestDto;



@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public void createPlan(PlanRequestDto dto) {
        // 필수 데이터 검증
        if (dto.getTitle() == null || dto.getTitle().isBlank() ||
                dto.getStartDate() == null ||
                dto.getEndDate() == null ||
                dto.getCategory() == null || dto.getCategory().isBlank() ||
                dto.getLocations() == null || dto.getLocations().isEmpty() ||
                dto.getUsers() == null || dto.getUsers().isEmpty()) {
            throw new InvalidPlanDataException("필수 입력 항목이 누락되었습니다.");
        }
        
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new LogicalPlanDataException("종료일은 시작일 이후여야 합니다.");
        }

        PlanEntity planEntity = toEntity(dto);
        planRepository.save(planEntity);

    }

    private PlanEntity toEntity(PlanRequestDto dto) {
        return new PlanEntity(
                null,
                dto.getTitle(),
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getCategory(),
                dto.getLocations() != null ? String.join(",", dto.getLocations()) : "",
                null
        );
    }
}
