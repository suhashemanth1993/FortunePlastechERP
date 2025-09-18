package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.domain.PurchaseOrder;
import com.fortune.fortuneplastecherp.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/purchase-orders")
@CrossOrigin(origins = "http://localhost:3002")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @PostMapping
    public ResponseEntity<PurchaseOrder> createPurchaseOrder(@RequestBody CreatePORequest request) {
        PurchaseOrder po = purchaseOrderService.createPurchaseOrder(request.getSupplierId(), request.getLineItems());
        return ResponseEntity.ok(po);
    }

    @GetMapping
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return purchaseOrderService.getAllPurchaseOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseOrder> getPurchaseOrderById(@PathVariable Long id) {
        Optional<PurchaseOrder> po = purchaseOrderService.getPurchaseOrderById(id);
        return po.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/link-batch")
    public ResponseEntity<PurchaseOrder> linkBatchToPO(@RequestParam Long batchId, @RequestParam Long poId) {
        PurchaseOrder po = purchaseOrderService.linkBatchToPO(batchId, poId);
        return ResponseEntity.ok(po);
    }

    public static class CreatePORequest {
        private Long supplierId;
        private List<PurchaseOrderService.LineItemRequest> lineItems;
        public Long getSupplierId() { return supplierId; }
        public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
        public List<PurchaseOrderService.LineItemRequest> getLineItems() { return lineItems; }
        public void setLineItems(List<PurchaseOrderService.LineItemRequest> lineItems) { this.lineItems = lineItems; }
    }
}
