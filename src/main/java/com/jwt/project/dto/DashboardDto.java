package com.jwt.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {
    private BigDecimal totalSpending;
    private BigDecimal monthlySpending;
    private Map<String, BigDecimal> categoryWiseSpending;
    private ExpenseDto highestExpense;
}
