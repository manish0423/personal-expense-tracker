package com.jwt.project.service;

import com.jwt.project.dto.DashboardDto;
import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.User;
import com.jwt.project.mapper.ExpenseMapper;
import com.jwt.project.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    @Cacheable(value = "dashboard", key = "#user.id")
    public DashboardDto getDashboard(User user) {
        DashboardDto dashboard = new DashboardDto();

        // Total spending
        BigDecimal totalSpending = expenseRepository.getTotalSpending(user)
                .orElse(BigDecimal.ZERO);
        dashboard.setTotalSpending(totalSpending);

        // Monthly spending (use month name like "MAY" to match how month is stored)
        String currentMonth = LocalDate.now().getMonth().toString();
        BigDecimal monthlySpending = expenseRepository.getMonthlySpending(user, currentMonth)
                .orElse(BigDecimal.ZERO);
        dashboard.setMonthlySpending(monthlySpending);

        // Highest expense
        expenseRepository.getHighestExpense(user)
                .ifPresent(expense -> dashboard.setHighestExpense(expenseMapper.toDto(expense)));

        // Category-wise spending placeholder
        dashboard.setCategoryWiseSpending(new HashMap<>());

        return dashboard;
    }

    @Cacheable(value = "recentExpenses", key = "#user.id")
    public java.util.List<ExpenseDto> getRecentExpenses(User user) {
        return expenseRepository.getRecentExpenses(user).stream()
                .map(expenseMapper::toDto)
                .toList();
    }

    @CacheEvict(value = {"dashboard", "recentExpenses"}, key = "#user.id")
    public void invalidateCache(User user) {
        // This method clears cache when expenses change
    }
}
