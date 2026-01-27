package com.defterio.dto;

import com.defterio.entity.ContactType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ContactRequest {
    @NotNull(message = "Type is required")
    private ContactType type;

    @NotBlank(message = "Name is required")
    private String name;

    private String taxNo;
    private String phone;
    private String email;
    private String address;
}
