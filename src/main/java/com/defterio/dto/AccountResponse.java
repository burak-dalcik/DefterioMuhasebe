package com.defterio.dto;

import com.defterio.entity.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private Long id;
    private AccountType type;
    private String name;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
