package com.defterio.service;

import com.defterio.dto.CategoryTotalResponse;
import com.defterio.dto.MonthlyTotalResponse;
import com.defterio.dto.SummaryReportResponse;
import com.defterio.entity.TransactionType;
import com.defterio.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final TransactionRepository transactionRepository;

    public SummaryReportResponse getSummary(LocalDate from, LocalDate to, String currency) {
        validateDateRange(from, to);

        BigDecimal totalIncome = transactionRepository.sumByTypeAndDateRangeAndCurrency(
                TransactionType.INCOME, from, to, currency
        );
        if (totalIncome == null) {
            totalIncome = BigDecimal.ZERO;
        }

        BigDecimal totalExpense = transactionRepository.sumByTypeAndDateRangeAndCurrency(
                TransactionType.EXPENSE, from, to, currency
        );
        if (totalExpense == null) {
            totalExpense = BigDecimal.ZERO;
        }

        BigDecimal net = totalIncome.subtract(totalExpense);

        return SummaryReportResponse.builder()
                .from(from)
                .to(to)
                .currency(currency)
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .net(net)
                .build();
    }

    public List<CategoryTotalResponse> getByCategory(LocalDate from, LocalDate to, TransactionType type, String currency) {
        validateDateRange(from, to);

        List<Object[]> results = transactionRepository.sumByCategoryAndTypeAndDateRangeAndCurrency(
                type, from, to, currency
        );

        return results.stream()
                .map(row -> CategoryTotalResponse.builder()
                        .categoryId(((Number) row[0]).longValue())
                        .categoryName((String) row[1])
                        .total((BigDecimal) row[2])
                        .build())
                .collect(Collectors.toList());
    }

    public List<MonthlyTotalResponse> getMonthly(int year, TransactionType type, String currency) {
        List<Object[]> results = transactionRepository.sumByMonthAndTypeAndYearAndCurrency(
                type.name(), year, currency
        );

        return results.stream()
                .map(row -> MonthlyTotalResponse.builder()
                        .month((String) row[0])
                        .total((BigDecimal) row[1])
                        .build())
                .collect(Collectors.toList());
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("From and to dates are required");
        }
        if (to.isBefore(from)) {
            throw new IllegalArgumentException("To date must be greater than or equal to from date");
        }
    }
}
