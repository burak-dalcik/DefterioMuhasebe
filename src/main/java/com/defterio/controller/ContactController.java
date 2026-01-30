package com.defterio.controller;

import com.defterio.dto.ContactRequest;
import com.defterio.dto.ContactResponse;
import com.defterio.entity.ContactType;
import com.defterio.service.ContactService;
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
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Tag(name = "Contacts", description = "Contact management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class ContactController {

    private final ContactService contactService;

    @GetMapping
    @Operation(summary = "Get all contacts with optional filters")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) ContactType type,
            Pageable pageable) {
        Page<ContactResponse> contacts = contactService.findAll(query, type, pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("data", contacts.getContent());
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", contacts.getNumber());
        meta.put("size", contacts.getSize());
        meta.put("totalElements", contacts.getTotalElements());
        meta.put("totalPages", contacts.getTotalPages());
        meta.put("supplierCount", contactService.countSuppliers());
        result.put("meta", meta);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get contact by ID")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        ContactResponse response = contactService.findById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new contact")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update contact by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody ContactRequest request) {
        ContactResponse response = contactService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        contactService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
