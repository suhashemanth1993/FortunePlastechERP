package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.domain.Supplier;
import com.fortune.fortuneplastecherp.repository.BatchRepository;
import com.fortune.fortuneplastecherp.repository.RawMaterialRepository;
import com.fortune.fortuneplastecherp.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BatchService {
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private SupplierRepository supplierRepository;

    public Batch receiveBatch(Long rawMaterialId, Long supplierId, Double quantity, Double costPerUnit, LocalDate receiptDate) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        Batch batch = new Batch();
        batch.setBatchId(UUID.randomUUID().toString());
        batch.setRawMaterial(rawMaterial);
        batch.setSupplier(supplier);
        batch.setQuantity(quantity);
        batch.setCostPerUnit(costPerUnit);
        batch.setReceiptDate(receiptDate != null ? receiptDate : LocalDate.now());
        return batchRepository.save(batch);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public Optional<Batch> getBatchById(Long id) {
        return batchRepository.findById(id);
    }

    public Batch updateBatch(Long id, Double quantity, Double costPerUnit) {
        Batch batch = batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Batch not found"));
        batch.setQuantity(quantity);
        batch.setCostPerUnit(costPerUnit);
        return batchRepository.save(batch);
    }

    public void deleteBatch(Long id) {
        batchRepository.deleteById(id);
    }
}
