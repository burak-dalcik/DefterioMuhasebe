package com.defterio.dto;

import com.defterio.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotNull(message = "Type is required")
    private CategoryType type;

    @NotBlank(message = "Name is required")
    private String name;
}
