package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.*;
import com.fortune.fortuneplastecherp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PurchaseOrderService {
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private PurchaseOrderLineItemRepository lineItemRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private BatchRepository batchRepository;

    @Transactional
    public PurchaseOrder createPurchaseOrder(Long supplierId, List<LineItemRequest> items) {
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        PurchaseOrder po = new PurchaseOrder();
        po.setPoNumber(UUID.randomUUID().toString());
        po.setSupplier(supplier);
        po.setOrderDate(LocalDate.now());
        po.setStatus("OPEN");
        purchaseOrderRepository.save(po);
        for (LineItemRequest item : items) {
            RawMaterial rm = rawMaterialRepository.findById(item.getRawMaterialId())
                    .orElseThrow(() -> new RuntimeException("Raw material not found"));
            PurchaseOrderLineItem li = new PurchaseOrderLineItem();
            li.setPurchaseOrder(po);
            li.setRawMaterial(rm);
            li.setQuantity(item.getQuantity());
            li.setUnitPrice(item.getUnitPrice());
            lineItemRepository.save(li);
        }
        return purchaseOrderRepository.findById(po.getId()).get();
    }

    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }

    public Optional<PurchaseOrder> getPurchaseOrderById(Long id) {
        return purchaseOrderRepository.findById(id);
    }

    public PurchaseOrder linkBatchToPO(Long batchId, Long poId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch not found"));
        PurchaseOrder po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() -> new RuntimeException("PO not found"));
        batch.setPurchaseOrder(po);
        batchRepository.save(batch);
        return po;
    }

    public static class LineItemRequest {
        private Long rawMaterialId;
        private Double quantity;
        private Double unitPrice;
        public Long getRawMaterialId() { return rawMaterialId; }
        public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
        public Double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
    }
}
