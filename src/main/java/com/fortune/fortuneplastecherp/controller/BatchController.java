package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.service.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/batches")
public class BatchController {
    @Autowired
    private BatchService batchService;

    @PostMapping
    public ResponseEntity<Batch> receiveBatch(
            @RequestParam Long rawMaterialId,
            @RequestParam Long supplierId,
            @RequestParam Double quantity,
            @RequestParam Double costPerUnit,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receiptDate
    ) {
        Batch batch = batchService.receiveBatch(rawMaterialId, supplierId, quantity, costPerUnit, receiptDate);
        return ResponseEntity.ok(batch);
    }

    @GetMapping
    public List<Batch> getAllBatches() {
        return batchService.getAllBatches();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Batch> getBatchById(@PathVariable Long id) {
        Optional<Batch> batch = batchService.getBatchById(id);
        return batch.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(
            @PathVariable Long id,
            @RequestParam Double quantity,
            @RequestParam Double costPerUnit
    ) {
        Batch updated = batchService.updateBatch(id, quantity, costPerUnit);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBatch(@PathVariable Long id) {
        batchService.deleteBatch(id);
        return ResponseEntity.noContent().build();
    }
}
