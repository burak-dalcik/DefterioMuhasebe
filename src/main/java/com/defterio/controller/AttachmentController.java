package com.defterio.controller;

import com.defterio.dto.AttachmentResponse;
import com.defterio.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Attachments", description = "Attachment management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(value = "/transactions/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload attachment to transaction")
    public ResponseEntity<Map<String, Object>> upload(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        String uploadedBy = authentication.getName();
        AttachmentResponse response = attachmentService.upload(id, file, uploadedBy);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/transactions/{id}/attachments")
    @Operation(summary = "Get all attachments for a transaction")
    public ResponseEntity<Map<String, Object>> getByTransactionId(@PathVariable Long id) {
        List<AttachmentResponse> attachments = attachmentService.findByTransactionId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", attachments);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/purchases/{id}/attachments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload attachment to purchase")
    public ResponseEntity<Map<String, Object>> uploadPurchase(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {
        String uploadedBy = authentication.getName();
        AttachmentResponse response = attachmentService.uploadPurchase(id, file, uploadedBy);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/purchases/{id}/attachments")
    @Operation(summary = "Get all attachments for a purchase")
    public ResponseEntity<Map<String, Object>> getByPurchaseId(@PathVariable Long id) {
        List<AttachmentResponse> attachments = attachmentService.findByPurchaseId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("data", attachments);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/attachments/{id}/download")
    @Operation(summary = "Download attachment")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        AttachmentResponse attachmentInfo = attachmentService.getAttachmentInfo(id);
        Resource resource = attachmentService.download(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachmentInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        String.format("attachment; filename=\"%s\"", attachmentInfo.getFileName()))
                .body(resource);
    }

    @DeleteMapping("/attachments/{id}")
    @Operation(summary = "Delete attachment")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        attachmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
