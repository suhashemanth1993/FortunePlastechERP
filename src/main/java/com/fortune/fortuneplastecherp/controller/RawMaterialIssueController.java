package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.domain.RawMaterialIssue;
import com.fortune.fortuneplastecherp.service.RawMaterialIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "http://localhost:3002")
public class RawMaterialIssueController {
    @Autowired
    private RawMaterialIssueService issueService;

    @PostMapping
    public ResponseEntity<RawMaterialIssue> issueRawMaterial(@RequestBody IssueRequest request) {
        RawMaterialIssue issue = issueService.issueRawMaterialFIFO(request.getProductionOrderId(), request.getRawMaterialId(), request.getQuantity());
        return ResponseEntity.ok(issue);
    }

    @GetMapping("/{productionOrderId}")
    public List<RawMaterialIssue> getIssuesByProductionOrder(@PathVariable String productionOrderId) {
        return issueService.getIssuesByProductionOrder(productionOrderId);
    }

    @GetMapping("/fifo-suggestion")
    public ResponseEntity<List<Map<String, Object>>> suggestFifoBatches(@RequestParam Long rawMaterialId, @RequestParam Double quantity) {
        List<Map<String, Object>> suggestions = issueService.suggestFifoBatches(rawMaterialId, quantity);
        return ResponseEntity.ok(suggestions);
    }

    public static class IssueRequest {
        private String productionOrderId;
        private Long rawMaterialId;
        private Double quantity;
        public String getProductionOrderId() { return productionOrderId; }
        public void setProductionOrderId(String productionOrderId) { this.productionOrderId = productionOrderId; }
        public Long getRawMaterialId() { return rawMaterialId; }
        public void setRawMaterialId(Long rawMaterialId) { this.rawMaterialId = rawMaterialId; }
        public Double getQuantity() { return quantity; }
        public void setQuantity(Double quantity) { this.quantity = quantity; }
    }
}
