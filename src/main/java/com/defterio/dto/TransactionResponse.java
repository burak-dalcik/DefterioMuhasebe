package com.defterio.dto;

import com.defterio.entity.TransactionSubtype;
import com.defterio.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private TransactionType type;
    private TransactionSubtype subtype;
    private BigDecimal amount;
    private String currency;
    private LocalDate date;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Long accountId;
    private String accountName;
    private Long contactId;
    private String contactName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
