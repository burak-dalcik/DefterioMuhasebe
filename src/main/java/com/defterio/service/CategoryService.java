package com.defterio.service;

import com.defterio.dto.CategoryRequest;
import com.defterio.dto.CategoryResponse;
import com.defterio.entity.Category;
import com.defterio.entity.CategoryType;
import com.defterio.exception.ConflictException;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Page<CategoryResponse> findAll(CategoryType type, Pageable pageable) {
        Page<Category> categories;
        if (type != null) {
            categories = categoryRepository.findByType(type, pageable);
        } else {
            categories = categoryRepository.findAll(pageable);
        }
        return categories.map(this::toResponse);
    }

    public CategoryResponse findById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.findByTypeAndName(request.getType(), request.getName()).isPresent()) {
            throw new ConflictException("Category with same type and name already exists");
        }

        Category category = Category.builder()
                .type(request.getType())
                .name(request.getName())
                .build();

        category = categoryRepository.save(category);
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getType().equals(request.getType()) || !category.getName().equals(request.getName())) {
            if (categoryRepository.findByTypeAndName(request.getType(), request.getName()).isPresent()) {
                throw new ConflictException("Category with same type and name already exists");
            }
        }

        category.setType(request.getType());
        category.setName(request.getName());

        category = categoryRepository.save(category);
        return toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (hasTransactions(category.getId())) {
            throw new ConflictException("Cannot delete category with existing transactions");
        }

        categoryRepository.delete(category);
    }

    private boolean hasTransactions(Long categoryId) {
        return false;
    }

    private CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .type(category.getType())
                .name(category.getName())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }
}
