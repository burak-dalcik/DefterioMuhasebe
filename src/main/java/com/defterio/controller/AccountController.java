package com.defterio.controller;

import com.defterio.dto.AccountRequest;
import com.defterio.dto.AccountResponse;
import com.defterio.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Accounts", description = "Account management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Get all accounts")
    public ResponseEntity<Map<String, Object>> getAll(Pageable pageable) {
        Page<AccountResponse> accounts = accountService.findAll(pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("data", accounts.getContent());
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", accounts.getNumber());
        meta.put("size", accounts.getSize());
        meta.put("totalElements", accounts.getTotalElements());
        meta.put("totalPages", accounts.getTotalPages());
        result.put("meta", meta);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new account")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
