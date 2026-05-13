package com.jwt.project.mapper;

import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    private final CategoryMapper categoryMapper;

    public ExpenseMapper(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public ExpenseDto toDto(Expense expense) {
        if (expense == null) {
            return null;
        }
        ExpenseDto dto = new ExpenseDto();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setAmount(expense.getAmount());
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setMonth(expense.getMonth());
        dto.setCategory(categoryMapper.toDto(expense.getCategory()));
        dto.setDescription(expense.getDescription());
        dto.setCreatedAt(expense.getCreatedAt());
        return dto;
    }

    public Expense toEntity(ExpenseDto dto) {
        if (dto == null) {
            return null;
        }
        Expense expense = new Expense();
        expense.setId(dto.getId());
        expense.setTitle(dto.getTitle());
        expense.setAmount(dto.getAmount());
        expense.setExpenseDate(dto.getExpenseDate());
        expense.setMonth(dto.getMonth());
        expense.setCategory(categoryMapper.toEntity(dto.getCategory()));
        expense.setDescription(dto.getDescription());
        expense.setCreatedAt(dto.getCreatedAt());
        return expense;
    }
}
