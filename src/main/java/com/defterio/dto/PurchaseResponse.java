package com.defterio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponse {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private Long accountId;
    private String accountName;
    private LocalDate date;
    private String currency;
    private String note;
    private BigDecimal total;
    private Long transactionId;
    private List<PurchaseLineResponse> lines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
