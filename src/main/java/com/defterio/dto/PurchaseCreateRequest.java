package com.defterio.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PurchaseCreateRequest {
    @NotNull(message = "Supplier ID is required")
    private Long supplierId;

    @NotNull(message = "Account ID is required")
    private Long accountId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String currency;

    @Size(max = 1000, message = "Note must not exceed 1000 characters")
    private String note;

    @NotEmpty(message = "At least one line is required")
    @Valid
    private List<PurchaseLineRequest> lines;
}
