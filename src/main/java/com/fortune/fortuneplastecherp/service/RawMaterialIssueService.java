package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.*;
import com.fortune.fortuneplastecherp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RawMaterialIssueService {
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private RawMaterialRepository rawMaterialRepository;
    @Autowired
    private RawMaterialIssueRepository issueRepository;
    @Autowired
    private RawMaterialIssueBatchRepository issueBatchRepository;

    @Transactional
    public RawMaterialIssue issueRawMaterialFIFO(String productionOrderId, Long rawMaterialId, Double quantity) {
        RawMaterial rawMaterial = rawMaterialRepository.findById(rawMaterialId)
                .orElseThrow(() -> new RuntimeException("Raw Material not found"));
        List<Batch> batches = batchRepository.findAll().stream()
                .filter(b -> b.getRawMaterial().getId().equals(rawMaterialId) && b.getQuantity() > 0)
                .sorted(Comparator.comparing(Batch::getReceiptDate))
                .collect(Collectors.toList());
        double remaining = quantity;
        List<RawMaterialIssueBatch> batchIssues = new ArrayList<>();
        for (Batch batch : batches) {
            if (remaining <= 0) break;
            double issueQty = Math.min(batch.getQuantity(), remaining);
            batch.setQuantity(batch.getQuantity() - issueQty);
            RawMaterialIssueBatch issueBatch = new RawMaterialIssueBatch();
            issueBatch.setBatch(batch);
            issueBatch.setQuantityIssued(issueQty);
            batchIssues.add(issueBatch);
            remaining -= issueQty;
        }
        if (remaining > 0) {
            throw new RuntimeException("Insufficient stock for raw material");
        }
        RawMaterialIssue issue = new RawMaterialIssue();
        issue.setProductionOrderId(productionOrderId);
        issue.setRawMaterial(rawMaterial);
        issue.setIssueDate(LocalDate.now());
        issue.setTotalQuantity(quantity);
        issue.setBatchIssues(batchIssues);
        issue = issueRepository.save(issue);
        for (RawMaterialIssueBatch bib : batchIssues) {
            bib.setIssue(issue);
            issueBatchRepository.save(bib);
        }
        // Save updated batches
        batchRepository.saveAll(batches);
        return issue;
    }

    public List<RawMaterialIssue> getIssuesByProductionOrder(String productionOrderId) {
        return issueRepository.findByProductionOrderId(productionOrderId);
    }

    public List<Map<String, Object>> suggestFifoBatches(Long rawMaterialId, Double quantity) {
        List<Batch> batches = batchRepository.findAll().stream()
                .filter(b -> b.getRawMaterial().getId().equals(rawMaterialId) && b.getQuantity() > 0)
                .sorted(Comparator.comparing(Batch::getReceiptDate))
                .collect(Collectors.toList());
        double remaining = quantity;
        List<Map<String, Object>> suggestions = new ArrayList<>();
        for (Batch batch : batches) {
            if (remaining <= 0) break;
            double issueQty = Math.min(batch.getQuantity(), remaining);
            Map<String, Object> entry = new HashMap<>();
            entry.put("batchId", batch.getBatchId());
            entry.put("quantity", issueQty);
            suggestions.add(entry);
            remaining -= issueQty;
        }
        if (remaining > 0) {
            throw new RuntimeException("Insufficient stock for raw material");
        }
        return suggestions;
    }
}
