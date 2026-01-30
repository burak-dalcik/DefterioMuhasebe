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
    @Digits(integer = 16, fraction = 2, message = "Amount must have at most 16 integer and 2 decimal digits")
    private BigDecimal amount;

    @Size(min = 3, max = 3, message = "Currency must be a 3-letter code (e.g. TRY, USD)")
    @Pattern(regexp = "[A-Z]{3}", message = "Currency must be uppercase (e.g. TRY, USD)")
    private String currency;

    @NotNull(message = "Date is required")
    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDate date;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    private Long contactId;
}
