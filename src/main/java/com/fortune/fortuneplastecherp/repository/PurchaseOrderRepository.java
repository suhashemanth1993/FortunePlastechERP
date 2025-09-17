package com.fortune.fortuneplastecherp.repository;

import com.fortune.fortuneplastecherp.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    PurchaseOrder findByPoNumber(String poNumber);
}
