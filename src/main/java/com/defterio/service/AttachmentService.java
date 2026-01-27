package com.defterio.service;

import com.defterio.dto.AttachmentResponse;
import com.defterio.entity.Attachment;
import com.defterio.entity.OwnerType;
import com.defterio.entity.Transaction;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.AttachmentRepository;
import com.defterio.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "application/pdf",
            "image/jpeg",
            "image/png"
    );

    private final AttachmentRepository attachmentRepository;
    private final TransactionRepository transactionRepository;
    private final StorageService storageService;

    @Transactional
    public AttachmentResponse upload(Long transactionId, MultipartFile file, String uploadedBy) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        return upload(OwnerType.TRANSACTION, transactionId, file, uploadedBy);
    }

    @Transactional
    public AttachmentResponse uploadPurchase(Long purchaseId, MultipartFile file, String uploadedBy) {
        return upload(OwnerType.PURCHASE, purchaseId, file, uploadedBy);
    }

    @Transactional
    public AttachmentResponse upload(OwnerType ownerType, Long ownerId, MultipartFile file, String uploadedBy) {
        validateFile(file);

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("File name is required");
        }

        String sanitizedFileName = sanitizeFileName(originalFileName);
        String storageKey = generateStorageKey(ownerType, ownerId, sanitizedFileName);

        try {
            storageService.save(file.getInputStream(), file.getSize(), file.getContentType(), storageKey);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        Attachment attachment = Attachment.builder()
                .ownerType(ownerType)
                .ownerId(ownerId)
                .fileName(originalFileName)
                .contentType(file.getContentType())
                .size(file.getSize())
                .storageKey(storageKey)
                .uploadedAt(Instant.now())
                .uploadedBy(uploadedBy)
                .build();

        attachment = attachmentRepository.save(attachment);
        return toResponse(attachment);
    }

    public List<AttachmentResponse> findByTransactionId(Long transactionId) {
        return findByOwner(OwnerType.TRANSACTION, transactionId);
    }

    public List<AttachmentResponse> findByPurchaseId(Long purchaseId) {
        return findByOwner(OwnerType.PURCHASE, purchaseId);
    }

    public List<AttachmentResponse> findByOwner(OwnerType ownerType, Long ownerId) {
        List<Attachment> attachments = attachmentRepository.findByOwnerTypeAndOwnerId(ownerType, ownerId);
        return attachments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Resource download(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));
        return storageService.loadAsResource(attachment.getStorageKey());
    }

    public AttachmentResponse getAttachmentInfo(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));
        return toResponse(attachment);
    }

    @Transactional
    public void delete(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new NotFoundException("Attachment not found"));

        storageService.delete(attachment.getStorageKey());
        attachmentRepository.delete(attachment);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size (20MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("File type not allowed. Allowed types: PDF, JPEG, PNG");
        }
    }

    private String sanitizeFileName(String fileName) {
        String sanitized = fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        return Paths.get(sanitized).getFileName().toString();
    }

    private String generateStorageKey(OwnerType ownerType, Long ownerId, String fileName) {
        String uuid = UUID.randomUUID().toString();
        String folder = ownerType == OwnerType.TRANSACTION ? "transactions" : "purchases";
        return String.format("%s/%d/%s_%s", folder, ownerId, uuid, fileName);
    }

    private AttachmentResponse toResponse(Attachment attachment) {
        return AttachmentResponse.builder()
                .id(attachment.getId())
                .ownerType(attachment.getOwnerType())
                .ownerId(attachment.getOwnerId())
                .fileName(attachment.getFileName())
                .contentType(attachment.getContentType())
                .size(attachment.getSize())
                .uploadedAt(attachment.getUploadedAt())
                .uploadedBy(attachment.getUploadedBy())
                .build();
    }
}
