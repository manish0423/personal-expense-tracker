package com.jwt.project.service;

import com.jwt.project.dto.CreateExpenseRequest;
import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.Category;
import com.jwt.project.entity.Expense;
import com.jwt.project.entity.User;
import com.jwt.project.mapper.ExpenseMapper;
import com.jwt.project.repository.CategoryRepository;
import com.jwt.project.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final ExpenseMapper expenseMapper;
    private final DashboardService dashboardService;

    public ExpenseDto createExpense(CreateExpenseRequest request, User user) {
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));

        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setMonth(request.getExpenseDate().getMonth().toString());
        expense.setCategory(category);
        expense.setDescription(request.getDescription());
        expense.setUser(user);

        Expense saved = expenseRepository.save(expense);
        dashboardService.invalidateCache(user);
        return expenseMapper.toDto(saved);
    }

    public List<ExpenseDto> getAllExpenses(User user) {
        return expenseRepository.findByUser(user).stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }

    public ExpenseDto getExpenseById(Long id, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        return expenseMapper.toDto(expense);
    }

    public ExpenseDto updateExpense(Long id, CreateExpenseRequest request, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new RuntimeException("Category not found or does not belong to user"));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setMonth(request.getExpenseDate().getMonth().toString());
        expense.setCategory(category);
        expense.setDescription(request.getDescription());

        Expense saved = expenseRepository.save(expense);
        dashboardService.invalidateCache(user);
        return expenseMapper.toDto(saved);
    }

    public void deleteExpense(Long id, User user) {
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
        dashboardService.invalidateCache(user);
    }

    public List<ExpenseDto> getFilteredExpenses(User user, String month, Long categoryId,
                                             BigDecimal minAmount, BigDecimal maxAmount,
                                             LocalDate startDate, LocalDate endDate,
                                             String sortBy, String sortDirection) {
        // Get filtered expenses
        List<Expense> expenses = expenseRepository.findByUserWithFilters(
                user, month, categoryId, minAmount, maxAmount, startDate, endDate);

        // Apply sorting
        Sort sort = createSort(sortBy, sortDirection);
        if (sort != null) {
            // For in-memory sorting since JPQL doesn't support dynamic sorting easily
            expenses.sort((e1, e2) -> {
                int result = 0;
                switch (sortBy.toLowerCase()) {
                    case "date":
                        result = e1.getExpenseDate().compareTo(e2.getExpenseDate());
                        break;
                    case "amount":
                        result = e1.getAmount().compareTo(e2.getAmount());
                        break;
                    case "title":
                        result = e1.getTitle().compareTo(e2.getTitle());
                        break;
                    default:
                        result = e1.getExpenseDate().compareTo(e2.getExpenseDate()); // default to date
                }
                return sortDirection.equalsIgnoreCase("DESC") ? -result : result;
            });
        }

        return expenses.stream()
                .map(expenseMapper::toDto)
                .collect(Collectors.toList());
    }

    private Sort createSort(String sortBy, String sortDirection) {
        if (sortBy == null || sortDirection == null) {
            return null;
        }

        Sort.Direction direction = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        switch (sortBy.toLowerCase()) {
            case "date":
                return Sort.by(direction, "expenseDate");
            case "amount":
                return Sort.by(direction, "amount");
            case "title":
                return Sort.by(direction, "title");
            default:
                return Sort.by(direction, "expenseDate"); // default
        }
    }
}
