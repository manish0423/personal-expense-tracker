package com.jwt.project.controller;

import com.jwt.project.dto.CreateExpenseRequest;
import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.User;
import com.jwt.project.security.UserPrincipal;
import com.jwt.project.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }

    @PostMapping
    public ResponseEntity<ExpenseDto> createExpense(@RequestBody CreateExpenseRequest request) {
        User user = getCurrentUser();
        ExpenseDto expense = expenseService.createExpense(request, user);
        return ResponseEntity.ok(expense);
    }

    @GetMapping
    public ResponseEntity<List<ExpenseDto>> getAllExpenses() {
        User user = getCurrentUser();
        List<ExpenseDto> expenses = expenseService.getAllExpenses(user);
        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseDto> getExpenseById(@PathVariable Long id) {
        User user = getCurrentUser();
        ExpenseDto expense = expenseService.getExpenseById(id, user);
        return ResponseEntity.ok(expense);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpenseDto> updateExpense(@PathVariable Long id, @RequestBody CreateExpenseRequest request) {
        User user = getCurrentUser();
        ExpenseDto expense = expenseService.updateExpense(id, request, user);
        return ResponseEntity.ok(expense);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        User user = getCurrentUser();
        expenseService.deleteExpense(id, user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<ExpenseDto>> getFilteredExpenses(
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        User user = getCurrentUser();
        List<ExpenseDto> expenses = expenseService.getFilteredExpenses(
                user, month, categoryId, minAmount, maxAmount, startDate, endDate, sortBy, sortDirection);
        return ResponseEntity.ok(expenses);
    }
}
