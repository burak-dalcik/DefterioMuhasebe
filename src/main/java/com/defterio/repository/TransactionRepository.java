package com.defterio.repository;

import com.defterio.entity.Transaction;
import com.defterio.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.type = :type AND t.date BETWEEN :from AND :to AND t.currency = :currency")
    BigDecimal sumByTypeAndDateRangeAndCurrency(
            @Param("type") TransactionType type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("currency") String currency
    );

    @Query("SELECT t.category.id, t.category.name, COALESCE(SUM(t.amount), 0) " +
           "FROM Transaction t " +
           "WHERE t.type = :type AND t.date BETWEEN :from AND :to AND t.currency = :currency " +
           "GROUP BY t.category.id, t.category.name " +
           "ORDER BY SUM(t.amount) DESC")
    List<Object[]> sumByCategoryAndTypeAndDateRangeAndCurrency(
            @Param("type") TransactionType type,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("currency") String currency
    );

    @Query(value = "SELECT TO_CHAR(t.date, 'YYYY-MM') as month, COALESCE(SUM(t.amount), 0) as total " +
           "FROM transactions t " +
           "WHERE t.type = :type " +
           "AND EXTRACT(YEAR FROM t.date) = :year " +
           "AND t.currency = :currency " +
           "GROUP BY TO_CHAR(t.date, 'YYYY-MM') " +
           "ORDER BY month", nativeQuery = true)
    List<Object[]> sumByMonthAndTypeAndYearAndCurrency(
            @Param("type") String type,
            @Param("year") int year,
            @Param("currency") String currency
    );
}
