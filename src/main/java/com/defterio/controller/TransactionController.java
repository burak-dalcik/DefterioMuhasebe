package com.defterio.controller;

import com.defterio.dto.TransactionCreateRequest;
import com.defterio.dto.TransactionResponse;
import com.defterio.dto.TransactionUpdateRequest;
import com.defterio.entity.TransactionSubtype;
import com.defterio.entity.TransactionType;
import com.defterio.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "Transaction management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Get all transactions with filters")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) TransactionSubtype subtype,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long contactId,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            Pageable pageable) {
        Page<TransactionResponse> transactions = transactionService.findAll(
                from, to, type, subtype, categoryId, accountId, contactId, minAmount, maxAmount, pageable
        );

        Map<String, Object> result = new HashMap<>();
        result.put("data", transactions.getContent());
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", transactions.getNumber());
        meta.put("size", transactions.getSize());
        meta.put("totalElements", transactions.getTotalElements());
        meta.put("totalPages", transactions.getTotalPages());
        result.put("meta", meta);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        TransactionResponse response = transactionService.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new transaction")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody TransactionCreateRequest request) {
        TransactionResponse response = transactionService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update transaction by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody TransactionUpdateRequest request) {
        TransactionResponse response = transactionService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete transaction by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
