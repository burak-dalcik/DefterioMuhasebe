package com.defterio.service;

import com.defterio.dto.BudgetRequest;
import com.defterio.dto.BudgetResponse;
import com.defterio.entity.Budget;
import com.defterio.entity.Category;
import com.defterio.entity.CategoryType;
import com.defterio.exception.ConflictException;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.BudgetRepository;
import com.defterio.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BudgetResponse> getBudgets(Integer year, Integer month, Long categoryId) {
        List<Budget> budgets;
        if (year != null && month != null) {
            budgets = budgetRepository.findByYearAndMonth(year, month);
        } else {
            budgets = budgetRepository.findAll();
        }

        if (categoryId != null) {
            budgets = budgets.stream()
                    .filter(b -> b.getCategory().getId().equals(categoryId))
                    .collect(Collectors.toList());
        }

        return budgets.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BudgetResponse create(BudgetRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new IllegalArgumentException("Budget category must be of type EXPENSE");
        }

        budgetRepository.findByCategoryIdAndYearAndMonth(
                        request.getCategoryId(), request.getYear(), request.getMonth())
                .ifPresent(b -> {
                    throw new ConflictException("Budget for this category and period already exists");
                });

        Budget budget = Budget.builder()
                .category(category)
                .year(request.getYear())
                .month(request.getMonth())
                .amount(request.getAmount())
                .build();

        budget = budgetRepository.save(budget);
        return toResponse(budget);
    }

    @Transactional
    public BudgetResponse update(Long id, BudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (category.getType() != CategoryType.EXPENSE) {
            throw new IllegalArgumentException("Budget category must be of type EXPENSE");
        }

        budgetRepository.findByCategoryIdAndYearAndMonth(
                        request.getCategoryId(), request.getYear(), request.getMonth())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new ConflictException("Budget for this category and period already exists");
                    }
                });

        budget.setCategory(category);
        budget.setYear(request.getYear());
        budget.setMonth(request.getMonth());
        budget.setAmount(request.getAmount());

        budget = budgetRepository.save(budget);
        return toResponse(budget);
    }

    @Transactional
    public void delete(Long id) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Budget not found"));
        budgetRepository.delete(budget);
    }

    private BudgetResponse toResponse(Budget budget) {
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .year(budget.getYear())
                .month(budget.getMonth())
                .amount(budget.getAmount())
                .build();
    }
}

