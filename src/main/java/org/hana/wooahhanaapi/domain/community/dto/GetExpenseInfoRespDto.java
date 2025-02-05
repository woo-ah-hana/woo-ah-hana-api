package org.hana.wooahhanaapi.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExpenseInfoRespDto {
    private List<GetExpensePlanInfoDto> planInfoList;
    private int numberOfPlans;
    private Long howMuchSpentThanLastQuarter;
    private Long thisQuarterExpense;
    private Long thisQuarterIncome;
    private int highestMonth;
    @Builder.Default
    private List<Long> monthlyExpenses = Arrays.asList(0L, 0L, 0L);
    private String highestPlanName;
    private Long highestPlanExpense;
    private String imageUrl;
}
