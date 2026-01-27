package com.defterio.dto;

import com.defterio.entity.TransactionSubtype;
import com.defterio.entity.TransactionType;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionUpdateRequest {
    @NotNull(message = "Type is required")
    private TransactionType type;

    @NotNull(message = "Subtype is required")
    private TransactionSubtype subtype;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    private String currency;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private Long contactId;
}
