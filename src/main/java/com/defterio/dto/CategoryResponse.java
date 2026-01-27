package com.defterio.dto;

import com.defterio.entity.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private CategoryType type;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
