package com.defterio.service;

import com.defterio.dto.PurchaseCreateRequest;
import com.defterio.dto.PurchaseLineRequest;
import com.defterio.dto.PurchaseLineResponse;
import com.defterio.dto.PurchaseResponse;
import com.defterio.dto.PurchaseUpdateRequest;
import com.defterio.entity.*;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private static final String PURCHASE_CATEGORY_NAME = "Alım (Purchase)";

    private final PurchaseRepository purchaseRepository;
    private final PurchaseLineRepository purchaseLineRepository;
    private final ContactRepository contactRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    public Page<PurchaseResponse> findAll(LocalDate from, LocalDate to, Long supplierId, Long accountId, Pageable pageable) {
        Page<Purchase> purchases = purchaseRepository.findByFilters(from, to, supplierId, accountId, pageable);
        return purchases.map(this::toResponse);
    }

    public PurchaseResponse findById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found"));
        return toResponse(purchase);
    }

    @Transactional
    public PurchaseResponse create(PurchaseCreateRequest request) {
        Contact supplier = contactRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        if (supplier.getType() != ContactType.SUPPLIER) {
            throw new IllegalArgumentException("Contact must be a SUPPLIER");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        List<PurchaseLine> lines = request.getLines().stream()
                .map(lineRequest -> {
                    Category category = categoryRepository.findById(lineRequest.getCategoryId())
                            .orElseThrow(() -> new NotFoundException("Category not found"));

                    if (category.getType() != CategoryType.EXPENSE) {
                        throw new IllegalArgumentException("Category must be EXPENSE type");
                    }

                    BigDecimal lineTotal = lineRequest.getQuantity().multiply(lineRequest.getUnitPrice());

                    return PurchaseLine.builder()
                            .description(lineRequest.getDescription())
                            .quantity(lineRequest.getQuantity())
                            .unitPrice(lineRequest.getUnitPrice())
                            .lineTotal(lineTotal)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal total = lines.stream()
                .map(PurchaseLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Category purchaseCategory = getOrCreatePurchaseCategory();

        Transaction transaction = Transaction.builder()
                .type(TransactionType.EXPENSE)
                .subtype(TransactionSubtype.PURCHASE)
                .amount(total)
                .currency(request.getCurrency() != null ? request.getCurrency() : "TRY")
                .date(request.getDate())
                .description(request.getNote())
                .category(purchaseCategory)
                .account(account)
                .contact(supplier)
                .build();

        transaction = transactionRepository.save(transaction);

        Purchase purchase = Purchase.builder()
                .supplier(supplier)
                .account(account)
                .date(request.getDate())
                .currency(request.getCurrency() != null ? request.getCurrency() : "TRY")
                .note(request.getNote())
                .total(total)
                .transaction(transaction)
                .build();

        purchase = purchaseRepository.save(purchase);

        for (PurchaseLine line : lines) {
            line.setPurchase(purchase);
        }
        purchaseLineRepository.saveAll(lines);

        return toResponse(purchase);
    }

    @Transactional
    public PurchaseResponse update(Long id, PurchaseUpdateRequest request) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        Contact supplier = contactRepository.findById(request.getSupplierId())
                .orElseThrow(() -> new NotFoundException("Supplier not found"));

        if (supplier.getType() != ContactType.SUPPLIER) {
            throw new IllegalArgumentException("Contact must be a SUPPLIER");
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        purchaseLineRepository.deleteByPurchaseId(id);

        List<PurchaseLine> lines = request.getLines().stream()
                .map(lineRequest -> {
                    Category category = categoryRepository.findById(lineRequest.getCategoryId())
                            .orElseThrow(() -> new NotFoundException("Category not found"));

                    if (category.getType() != CategoryType.EXPENSE) {
                        throw new IllegalArgumentException("Category must be EXPENSE type");
                    }

                    BigDecimal lineTotal = lineRequest.getQuantity().multiply(lineRequest.getUnitPrice());

                    return PurchaseLine.builder()
                            .purchase(purchase)
                            .description(lineRequest.getDescription())
                            .quantity(lineRequest.getQuantity())
                            .unitPrice(lineRequest.getUnitPrice())
                            .lineTotal(lineTotal)
                            .category(category)
                            .build();
                })
                .collect(Collectors.toList());

        BigDecimal total = lines.stream()
                .map(PurchaseLine::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        purchaseLineRepository.saveAll(lines);

        purchase.setSupplier(supplier);
        purchase.setAccount(account);
        purchase.setDate(request.getDate());
        if (request.getCurrency() != null) {
            purchase.setCurrency(request.getCurrency());
        }
        purchase.setNote(request.getNote());
        purchase.setTotal(total);

        Transaction transaction = purchase.getTransaction();
        transaction.setAmount(total);
        transaction.setDate(request.getDate());
        transaction.setAccount(account);
        transaction.setContact(supplier);
        if (request.getNote() != null) {
            transaction.setDescription(request.getNote());
        }
        transactionRepository.save(transaction);

        purchase = purchaseRepository.save(purchase);
        return toResponse(purchase);
    }

    @Transactional
    public void delete(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Purchase not found"));

        purchaseLineRepository.deleteByPurchaseId(id);
        transactionRepository.delete(purchase.getTransaction());
        purchaseRepository.delete(purchase);
    }

    private Category getOrCreatePurchaseCategory() {
        return categoryRepository.findByTypeAndName(CategoryType.EXPENSE, PURCHASE_CATEGORY_NAME)
                .orElseGet(() -> {
                    Category category = Category.builder()
                            .type(CategoryType.EXPENSE)
                            .name(PURCHASE_CATEGORY_NAME)
                            .build();
                    return categoryRepository.save(category);
                });
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        List<PurchaseLineResponse> lines = purchaseLineRepository.findByPurchaseId(purchase.getId())
                .stream()
                .map(line -> PurchaseLineResponse.builder()
                        .id(line.getId())
                        .description(line.getDescription())
                        .quantity(line.getQuantity())
                        .unitPrice(line.getUnitPrice())
                        .lineTotal(line.getLineTotal())
                        .categoryId(line.getCategory().getId())
                        .categoryName(line.getCategory().getName())
                        .build())
                .collect(Collectors.toList());

        return PurchaseResponse.builder()
                .id(purchase.getId())
                .supplierId(purchase.getSupplier().getId())
                .supplierName(purchase.getSupplier().getName())
                .accountId(purchase.getAccount().getId())
                .accountName(purchase.getAccount().getName())
                .date(purchase.getDate())
                .currency(purchase.getCurrency())
                .note(purchase.getNote())
                .total(purchase.getTotal())
                .transactionId(purchase.getTransaction().getId())
                .lines(lines)
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }
}
