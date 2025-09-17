package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.repository.BatchRepository;
import com.fortune.fortuneplastecherp.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StockAlertService {
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private BatchRepository batchRepository;

    public List<Map<String, Object>> getMaterialsBelowMinimumStock() {
        List<RawMaterial> materials = rawMaterialRepository.findAll();
        List<Batch> batches = batchRepository.findAll();
        List<Map<String, Object>> alerts = new ArrayList<>();
        for (RawMaterial rm : materials) {
            if (rm.getMinimumStockLevel() != null) {
                double totalStock = batches.stream()
                        .filter(b -> b.getRawMaterial().getId().equals(rm.getId()))
                        .mapToDouble(Batch::getQuantity).sum();
                if (totalStock < rm.getMinimumStockLevel()) {
                    Map<String, Object> alert = new HashMap<>();
                    alert.put("rawMaterialId", rm.getId());
                    alert.put("rawMaterialCode", rm.getCode());
                    alert.put("rawMaterialName", rm.getName());
                    alert.put("currentStock", totalStock);
                    alert.put("minimumStockLevel", rm.getMinimumStockLevel());
                    alerts.add(alert);
                }
            }
        }
        return alerts;
    }
}
