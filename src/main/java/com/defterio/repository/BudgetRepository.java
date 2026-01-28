package com.defterio.repository;

import com.defterio.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByYearAndMonth(Integer year, Integer month);

    Optional<Budget> findByCategoryIdAndYearAndMonth(Long categoryId, Integer year, Integer month);
}

