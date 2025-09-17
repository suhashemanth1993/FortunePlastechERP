package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.ReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportingController {
    @Autowired
    private ReportingService reportingService;

    @GetMapping("/stock-summary")
    public List<Map<String, Object>> getStockSummary() {
        return reportingService.getStockSummary();
    }

    @GetMapping("/supplier-purchase")
    public List<Map<String, Object>> getSupplierPurchaseReport(@RequestParam(required = false) Long supplierId) {
        return reportingService.getSupplierPurchaseReport(supplierId);
    }

    @GetMapping("/costing")
    public List<Map<String, Object>> getCostingReport() {
        return reportingService.getCostingReport();
    }
}
