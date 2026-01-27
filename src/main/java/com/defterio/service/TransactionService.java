package com.defterio.service;

import com.defterio.dto.TransactionCreateRequest;
import com.defterio.dto.TransactionResponse;
import com.defterio.dto.TransactionUpdateRequest;
import com.defterio.entity.*;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.*;
import com.defterio.repository.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final ContactRepository contactRepository;

    public Page<TransactionResponse> findAll(
            LocalDate from,
            LocalDate to,
            TransactionType type,
            TransactionSubtype subtype,
            Long categoryId,
            Long accountId,
            Long contactId,
            BigDecimal minAmount,
            BigDecimal maxAmount,
            Pageable pageable
    ) {
        Specification<Transaction> spec = TransactionSpecification.withFilters(
                from, to, type, subtype, categoryId, accountId, contactId, minAmount, maxAmount
        );

        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<Transaction> transactions = transactionRepository.findAll(spec, sortedPageable);
        return transactions.map(this::toResponse);
    }

    public TransactionResponse findById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse create(TransactionCreateRequest request) {
        validateTransaction(request.getType(), request.getSubtype(), request.getCategoryId(), request.getContactId());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Contact contact = null;
        if (request.getContactId() != null) {
            contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new NotFoundException("Contact not found"));
        }

        Transaction transaction = Transaction.builder()
                .type(request.getType())
                .subtype(request.getSubtype())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "TRY")
                .date(request.getDate())
                .description(request.getDescription())
                .category(category)
                .account(account)
                .contact(contact)
                .build();

        transaction = transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Transactional
    public TransactionResponse update(Long id, TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));

        validateTransaction(request.getType(), request.getSubtype(), request.getCategoryId(), request.getContactId());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Contact contact = null;
        if (request.getContactId() != null) {
            contact = contactRepository.findById(request.getContactId())
                    .orElseThrow(() -> new NotFoundException("Contact not found"));
        }

        transaction.setType(request.getType());
        transaction.setSubtype(request.getSubtype());
        transaction.setAmount(request.getAmount());
        if (request.getCurrency() != null) {
            transaction.setCurrency(request.getCurrency());
        }
        transaction.setDate(request.getDate());
        transaction.setDescription(request.getDescription());
        transaction.setCategory(category);
        transaction.setAccount(account);
        transaction.setContact(contact);

        transaction = transactionRepository.save(transaction);
        return toResponse(transaction);
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found"));
        transactionRepository.delete(transaction);
    }

    private void validateTransaction(TransactionType type, TransactionSubtype subtype, Long categoryId, Long contactId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        if (!category.getType().name().equals(type.name())) {
            throw new IllegalArgumentException(
                    String.format("Category type (%s) must match transaction type (%s)", category.getType(), type)
            );
        }

        if (subtype == TransactionSubtype.PURCHASE) {
            if (contactId == null) {
                throw new IllegalArgumentException("Contact is required for PURCHASE transactions");
            }

            Contact contact = contactRepository.findById(contactId)
                    .orElseThrow(() -> new NotFoundException("Contact not found"));

            if (contact.getType() != ContactType.SUPPLIER) {
                throw new IllegalArgumentException("Contact must be SUPPLIER for PURCHASE transactions");
            }
        }
    }

    private TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .type(transaction.getType())
                .subtype(transaction.getSubtype())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .accountId(transaction.getAccount().getId())
                .accountName(transaction.getAccount().getName())
                .contactId(transaction.getContact() != null ? transaction.getContact().getId() : null)
                .contactName(transaction.getContact() != null ? transaction.getContact().getName() : null)
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
