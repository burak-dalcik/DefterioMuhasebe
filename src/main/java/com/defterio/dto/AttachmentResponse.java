package com.defterio.dto;

import com.defterio.entity.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private OwnerType ownerType;
    private Long ownerId;
    private String fileName;
    private String contentType;
    private Long size;
    private Instant uploadedAt;
    private String uploadedBy;
}
