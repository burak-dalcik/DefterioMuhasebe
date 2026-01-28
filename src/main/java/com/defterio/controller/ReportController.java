package com.defterio.controller;

import com.defterio.dto.CategoryTotalResponse;
import com.defterio.dto.MonthlyTotalResponse;
import com.defterio.dto.SummaryReportResponse;
import com.defterio.entity.TransactionType;
import com.defterio.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Reports", description = "Transaction report endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/summary")
    @Operation(summary = "Get summary report (total income, expense, net)")
    public ResponseEntity<Map<String, Object>> getSummary(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "TRY") String currency) {
        SummaryReportResponse response = reportService.getSummary(from, to, currency);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/daily")
    @Operation(summary = "Get daily summary report")
    public ResponseEntity<Map<String, Object>> getDaily(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "TRY") String currency) {
        SummaryReportResponse response = reportService.getDaily(date, currency);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/weekly")
    @Operation(summary = "Get weekly summary report")
    public ResponseEntity<Map<String, Object>> getWeekly(
            @RequestParam("weekStart") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @RequestParam(defaultValue = "TRY") String currency) {
        SummaryReportResponse response = reportService.getWeekly(weekStart, currency);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-category")
    @Operation(summary = "Get transactions grouped by category")
    public ResponseEntity<Map<String, Object>> getByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam TransactionType type,
            @RequestParam(defaultValue = "TRY") String currency) {
        List<CategoryTotalResponse> response = reportService.getByCategory(from, to, type, currency);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/monthly")
    @Operation(summary = "Get monthly trend report")
    public ResponseEntity<Map<String, Object>> getMonthly(
            @RequestParam int year,
            @RequestParam TransactionType type,
            @RequestParam(defaultValue = "TRY") String currency) {
        List<MonthlyTotalResponse> response = reportService.getMonthly(year, type, currency);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }
}
