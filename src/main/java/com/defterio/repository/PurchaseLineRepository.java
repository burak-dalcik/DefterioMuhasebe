package com.defterio.repository;

import com.defterio.entity.PurchaseLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseLineRepository extends JpaRepository<PurchaseLine, Long> {
    List<PurchaseLine> findByPurchaseId(Long purchaseId);
    void deleteByPurchaseId(Long purchaseId);
}
