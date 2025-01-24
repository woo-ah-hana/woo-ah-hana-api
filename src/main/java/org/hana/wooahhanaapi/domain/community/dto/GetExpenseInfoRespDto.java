package org.hana.wooahhanaapi.domain.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetExpenseInfoRespDto {
    private List<String> planTitleList;
    private int numberOfPlans;
    private Long howMuchSpentThanLastQuarter;
    private Long thisQuarterExpense;
    private Long thisQuarterIncome;
    private int highestMonth;
    @Builder.Default
    private List<Long> monthlyExpenses = Arrays.asList(0L, 0L, 0L);
    private String highestPlanName;
    private Long highestPlanExpense;
}
