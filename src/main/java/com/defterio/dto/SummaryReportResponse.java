package com.defterio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryReportResponse {
    private LocalDate from;
    private LocalDate to;
    private String currency;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal net;
}
