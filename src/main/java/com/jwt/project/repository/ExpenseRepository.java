package com.jwt.project.repository;

import com.jwt.project.entity.Expense;
import com.jwt.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUser(User user);

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user")
    Optional<BigDecimal> getTotalSpending(@Param("user") User user);

    @Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user = :user AND e.month = :month")
    Optional<BigDecimal> getMonthlySpending(@Param("user") User user, @Param("month") String month);

    @Query("SELECT e FROM Expense e WHERE e.user = :user ORDER BY e.amount DESC LIMIT 1")
    Optional<Expense> getHighestExpense(@Param("user") User user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user ORDER BY e.expenseDate DESC LIMIT 5")
    List<Expense> getRecentExpenses(@Param("user") User user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user AND e.category.id = :categoryId")
    List<Expense> getExpensesByCategory(@Param("user") User user, @Param("categoryId") Long categoryId);

    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
           "AND (:month IS NULL OR e.month = :month) " +
           "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
           "AND (:minAmount IS NULL OR e.amount >= :minAmount) " +
           "AND (:maxAmount IS NULL OR e.amount <= :maxAmount) " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate)")
    List<Expense> findByUserWithFilters(@Param("user") User user,
                                        @Param("month") String month,
                                        @Param("categoryId") Long categoryId,
                                        @Param("minAmount") java.math.BigDecimal minAmount,
                                        @Param("maxAmount") java.math.BigDecimal maxAmount,
                                        @Param("startDate") java.time.LocalDate startDate,
                                        @Param("endDate") java.time.LocalDate endDate);
}
