package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.repository.BatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExpiryAlertService {
    @Autowired
    private BatchRepository batchRepository;

    public List<Map<String, Object>> getExpiredOrNearExpiryBatches(int daysThreshold) {
        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(daysThreshold);
        List<Batch> batches = batchRepository.findAll();
        return batches.stream()
                .filter(b -> b.getExpiryDate() != null && !b.getQuantity().equals(0.0))
                .filter(b -> b.getExpiryDate().isBefore(thresholdDate))
                .map(b -> {
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("batchId", b.getBatchId());
                    entry.put("rawMaterialId", b.getRawMaterial().getId());
                    entry.put("rawMaterialName", b.getRawMaterial().getName());
                    entry.put("quantity", b.getQuantity());
                    entry.put("expiryDate", b.getExpiryDate());
                    entry.put("location", b.getLocation());
                    entry.put("isExpired", b.getExpiryDate().isBefore(today));
                    return entry;
                })
                .collect(Collectors.toList());
    }
}
