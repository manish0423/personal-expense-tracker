package com.jwt.project.service;

import com.jwt.project.dto.CreateExpenseRequest;
import com.jwt.project.dto.ExpenseDto;
import com.jwt.project.entity.Category;
import com.jwt.project.entity.Expense;
import com.jwt.project.entity.User;
import com.jwt.project.mapper.ExpenseMapper;
import com.jwt.project.repository.CategoryRepository;
import com.jwt.project.repository.ExpenseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ExpenseMapper expenseMapper;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private ExpenseService expenseService;

    private User user;
    private Category category;
    private Expense expense;
    private ExpenseDto expenseDto;
    private CreateExpenseRequest createRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        category = new Category();
        category.setId(1L);
        category.setName("Food");
        category.setUser(user);

        expense = new Expense();
        expense.setId(1L);
        expense.setTitle("Lunch");
        expense.setAmount(BigDecimal.valueOf(15.50));
        expense.setExpenseDate(LocalDate.now());
        expense.setMonth("MAY");
        expense.setCategory(category);
        expense.setUser(user);

        expenseDto = new ExpenseDto();
        expenseDto.setId(1L);
        expenseDto.setTitle("Lunch");
        expenseDto.setAmount(BigDecimal.valueOf(15.50));

        createRequest = new CreateExpenseRequest();
        createRequest.setTitle("Lunch");
        createRequest.setAmount(BigDecimal.valueOf(15.50));
        createRequest.setExpenseDate(LocalDate.now());
        createRequest.setCategoryId(1L);
        createRequest.setDescription("Burger");
    }

    @Test
    void createExpense_Success() {
        // Arrange
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.toDto(any(Expense.class))).thenReturn(expenseDto);

        // Act
        ExpenseDto result = expenseService.createExpense(createRequest, user);

        // Assert
        assertNotNull(result);
        assertEquals("Lunch", result.getTitle());
        assertEquals(BigDecimal.valueOf(15.50), result.getAmount());
        verify(expenseRepository).save(any(Expense.class));
        verify(dashboardService).invalidateCache(user);
    }

    @Test
    void createExpense_CategoryNotFound_ThrowsException() {
        // Arrange
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> expenseService.createExpense(createRequest, user));
        assertEquals("Category not found or does not belong to user", exception.getMessage());
        verify(expenseRepository, never()).save(any(Expense.class));
    }

    @Test
    void getAllExpenses_Success() {
        // Arrange
        List<Expense> expenses = Arrays.asList(expense);
        when(expenseRepository.findByUser(user)).thenReturn(expenses);
        when(expenseMapper.toDto(any(Expense.class))).thenReturn(expenseDto);

        // Act
        List<ExpenseDto> result = expenseService.getAllExpenses(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Lunch", result.get(0).getTitle());
    }

    @Test
    void getExpenseById_Success() {
        // Arrange
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));
        when(expenseMapper.toDto(expense)).thenReturn(expenseDto);

        // Act
        ExpenseDto result = expenseService.getExpenseById(1L, user);

        // Assert
        assertNotNull(result);
        assertEquals("Lunch", result.getTitle());
    }

    @Test
    void getExpenseById_NotFound_ThrowsException() {
        // Arrange
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> expenseService.getExpenseById(1L, user));
        assertEquals("Expense not found", exception.getMessage());
    }

    @Test
    void updateExpense_Success() {
        // Arrange
        CreateExpenseRequest updateRequest = new CreateExpenseRequest();
        updateRequest.setTitle("Dinner");
        updateRequest.setAmount(BigDecimal.valueOf(25.00));
        updateRequest.setExpenseDate(LocalDate.now());
        updateRequest.setCategoryId(1L);

        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));
        when(categoryRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);
        when(expenseMapper.toDto(any(Expense.class))).thenReturn(expenseDto);

        // Act
        ExpenseDto result = expenseService.updateExpense(1L, updateRequest, user);

        // Assert
        assertNotNull(result);
        verify(expenseRepository).save(expense);
        verify(dashboardService).invalidateCache(user);
    }

    @Test
    void deleteExpense_Success() {
        // Arrange
        when(expenseRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(expense));

        // Act
        expenseService.deleteExpense(1L, user);

        // Assert
        verify(expenseRepository).delete(expense);
        verify(dashboardService).invalidateCache(user);
    }

    @Test
    void getFilteredExpenses_WithFilters() {
        // Arrange
        List<Expense> expenses = Arrays.asList(expense);
        when(expenseRepository.findByUserWithFilters(eq(user), eq("MAY"), eq(1L),
            any(BigDecimal.class), any(BigDecimal.class), any(LocalDate.class), any(LocalDate.class)))
            .thenReturn(expenses);
        when(expenseMapper.toDto(any(Expense.class))).thenReturn(expenseDto);

        // Act
        List<ExpenseDto> result = expenseService.getFilteredExpenses(user, "MAY", 1L,
            BigDecimal.valueOf(10), BigDecimal.valueOf(100), null, null, "date", "DESC");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
