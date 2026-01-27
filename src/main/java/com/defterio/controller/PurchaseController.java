package com.defterio.controller;

import com.defterio.dto.PurchaseCreateRequest;
import com.defterio.dto.PurchaseResponse;
import com.defterio.dto.PurchaseUpdateRequest;
import com.defterio.service.PurchaseService;
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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
@Tag(name = "Purchases", description = "Purchase management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    @Operation(summary = "Get all purchases with optional filters")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long accountId,
            Pageable pageable) {
        Page<PurchaseResponse> purchases = purchaseService.findAll(from, to, supplierId, accountId, pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("data", purchases.getContent());
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", purchases.getNumber());
        meta.put("size", purchases.getSize());
        meta.put("totalElements", purchases.getTotalElements());
        meta.put("totalPages", purchases.getTotalPages());
        result.put("meta", meta);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get purchase by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        PurchaseResponse response = purchaseService.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new purchase")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody PurchaseCreateRequest request) {
        PurchaseResponse response = purchaseService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update purchase by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody PurchaseUpdateRequest request) {
        PurchaseResponse response = purchaseService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete purchase by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        purchaseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
