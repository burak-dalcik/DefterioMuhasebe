package com.defterio.repository;

import com.defterio.entity.Category;
import com.defterio.entity.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Page<Category> findByType(CategoryType type, Pageable pageable);
    Optional<Category> findByTypeAndName(CategoryType type, String name);
}
