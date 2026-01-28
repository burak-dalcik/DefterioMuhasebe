package com.defterio.controller;

import com.defterio.dto.BudgetRequest;
import com.defterio.dto.BudgetResponse;
import com.defterio.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgets", description = "Category budget management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class BudgetController {

    private final BudgetService budgetService;

    @GetMapping
    @Operation(summary = "Get budgets with optional filters")
    public ResponseEntity<Map<String, Object>> getBudgets(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long categoryId) {
        List<BudgetResponse> budgets = budgetService.getBudgets(year, month, categoryId);
        Map<String, Object> result = new HashMap<>();
        result.put("data", budgets);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new budget")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update budget by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody BudgetRequest request) {
        BudgetResponse response = budgetService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete budget by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        budgetService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

