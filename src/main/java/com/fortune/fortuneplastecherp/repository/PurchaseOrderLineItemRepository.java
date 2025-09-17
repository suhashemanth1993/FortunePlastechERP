package com.fortune.fortuneplastecherp.repository;

import com.fortune.fortuneplastecherp.domain.PurchaseOrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderLineItemRepository extends JpaRepository<PurchaseOrderLineItem, Long> {
}
