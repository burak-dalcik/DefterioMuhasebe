package com.defterio.repository.specification;

import com.defterio.entity.Transaction;
import com.defterio.entity.TransactionSubtype;
import com.defterio.entity.TransactionType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<Transaction> withFilters(
            LocalDate from,
            LocalDate to,
            TransactionType type,
            TransactionSubtype subtype,
            Long categoryId,
            Long accountId,
            Long contactId,
            BigDecimal minAmount,
            BigDecimal maxAmount
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), from));
            }

            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), to));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (subtype != null) {
                predicates.add(cb.equal(root.get("subtype"), subtype));
            }

            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }

            if (accountId != null) {
                predicates.add(cb.equal(root.get("account").get("id"), accountId));
            }

            if (contactId != null) {
                predicates.add(cb.equal(root.get("contact").get("id"), contactId));
            }

            if (minAmount != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), minAmount));
            }

            if (maxAmount != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), maxAmount));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
