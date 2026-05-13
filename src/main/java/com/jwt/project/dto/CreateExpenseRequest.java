package com.jwt.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequest {
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private Long categoryId;
    private String description;
}
