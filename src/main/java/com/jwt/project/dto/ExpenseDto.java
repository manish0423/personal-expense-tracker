package com.jwt.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    private Long id;
    private String title;
    private BigDecimal amount;
    private LocalDate expenseDate;
    private String month;
    private CategoryDto category;
    private String description;
    private LocalDateTime createdAt;
}
