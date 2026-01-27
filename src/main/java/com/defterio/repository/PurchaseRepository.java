package com.defterio.repository;

import com.defterio.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    @Query("SELECT p FROM Purchase p WHERE " +
           "(:from IS NULL OR p.date >= :from) AND " +
           "(:to IS NULL OR p.date <= :to) AND " +
           "(:supplierId IS NULL OR p.supplier.id = :supplierId) AND " +
           "(:accountId IS NULL OR p.account.id = :accountId)")
    Page<Purchase> findByFilters(
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            @Param("supplierId") Long supplierId,
            @Param("accountId") Long accountId,
            Pageable pageable
    );
}
