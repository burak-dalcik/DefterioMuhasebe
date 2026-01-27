package com.defterio.dto;

import com.defterio.entity.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountRequest {
    @NotNull(message = "Type is required")
    private AccountType type;

    @NotBlank(message = "Name is required")
    private String name;

    private String currency;
}
