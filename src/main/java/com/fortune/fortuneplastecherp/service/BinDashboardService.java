package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BinDashboardService {
    @Autowired
    private BatchRepository batchRepository;

    public List<Map<String, Object>> getStockByMaterialBatch(Long rawMaterialId, String location) {
        List<Batch> batches = batchRepository.findAll();
        return batches.stream()
                .filter(batch -> (rawMaterialId == null || batch.getRawMaterial().getId().equals(rawMaterialId)))
                .filter(batch -> (location == null || (batch.getLocation() != null && batch.getLocation().equalsIgnoreCase(location))))
                .map(batch -> {
                    Map<String, Object> entry = new HashMap<>();
                    RawMaterial rm = batch.getRawMaterial();
                    entry.put("rawMaterialId", rm.getId());
                    entry.put("rawMaterialCode", rm.getCode());
                    entry.put("rawMaterialName", rm.getName());
                    entry.put("batchId", batch.getBatchId());
                    entry.put("quantity", batch.getQuantity());
                    entry.put("receiptDate", batch.getReceiptDate());
                    entry.put("location", batch.getLocation());
                    return entry;
                })
                .collect(Collectors.toList());
    }
}
