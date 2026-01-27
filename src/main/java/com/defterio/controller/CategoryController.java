package com.defterio.controller;

import com.defterio.dto.CategoryRequest;
import com.defterio.dto.CategoryResponse;
import com.defterio.entity.CategoryType;
import com.defterio.service.CategoryService;
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
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Category management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Get all categories with optional type filter")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(required = false) CategoryType type,
            Pageable pageable) {
        Page<CategoryResponse> categories = categoryService.findAll(type, pageable);
        Map<String, Object> result = new HashMap<>();
        result.put("data", categories.getContent());
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", categories.getNumber());
        meta.put("size", categories.getSize());
        meta.put("totalElements", categories.getTotalElements());
        meta.put("totalPages", categories.getTotalPages());
        result.put("meta", meta);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<Map<String, Object>> create(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.create(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update category by ID")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.update(id, request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete category by ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
