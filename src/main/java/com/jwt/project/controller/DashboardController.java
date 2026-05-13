package com.jwt.project.controller;

import com.jwt.project.dto.DashboardDto;
import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.User;
import com.jwt.project.security.UserPrincipal;
import com.jwt.project.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUser();
    }

    @GetMapping
    public ResponseEntity<DashboardDto> getDashboard() {
        User user = getCurrentUser();
        DashboardDto dashboard = dashboardService.getDashboard(user);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<ExpenseDto>> getRecentExpenses() {
        User user = getCurrentUser();
        List<ExpenseDto> expenses = dashboardService.getRecentExpenses(user);
        return ResponseEntity.ok(expenses);
    }
}
