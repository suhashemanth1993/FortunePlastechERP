package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.*;
import com.fortune.fortuneplastecherp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportingService {
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private RawMaterialIssueRepository issueRepository;

    public List<Map<String, Object>> getStockSummary() {
        List<Batch> batches = batchRepository.findAll();
        return batches.stream().map(batch -> {
            Map<String, Object> entry = new HashMap<>();
            entry.put("rawMaterialId", batch.getRawMaterial().getId());
            entry.put("rawMaterialName", batch.getRawMaterial().getName());
            entry.put("batchId", batch.getBatchId());
            entry.put("quantity", batch.getQuantity());
            entry.put("location", batch.getLocation());
            return entry;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> getSupplierPurchaseReport(Long supplierId) {
        List<PurchaseOrder> pos = supplierId == null ? purchaseOrderRepository.findAll() :
                purchaseOrderRepository.findAll().stream().filter(po -> po.getSupplier().getId().equals(supplierId)).collect(Collectors.toList());
        List<Map<String, Object>> report = new ArrayList<>();
        for (PurchaseOrder po : pos) {
            for (PurchaseOrderLineItem li : po.getLineItems()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("poNumber", po.getPoNumber());
                entry.put("supplierName", po.getSupplier().getName());
                entry.put("orderDate", po.getOrderDate());
                entry.put("rawMaterialName", li.getRawMaterial().getName());
                entry.put("quantity", li.getQuantity());
                entry.put("unitPrice", li.getUnitPrice());
                report.add(entry);
            }
        }
        return report;
    }

    public List<Map<String, Object>> getCostingReport() {
        List<RawMaterialIssue> issues = issueRepository.findAll();
        List<Map<String, Object>> report = new ArrayList<>();
        for (RawMaterialIssue issue : issues) {
            for (RawMaterialIssueBatch bib : issue.getBatchIssues()) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("productionOrderId", issue.getProductionOrderId());
                entry.put("rawMaterialName", issue.getRawMaterial().getName());
                entry.put("batchId", bib.getBatch().getBatchId());
                entry.put("quantityIssued", bib.getQuantityIssued());
                entry.put("costPerUnit", bib.getBatch().getCostPerUnit());
                entry.put("totalCost", bib.getQuantityIssued() * bib.getBatch().getCostPerUnit());
                report.add(entry);
            }
        }
        return report;
    }
}
