package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.StockAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stock-alerts")
public class StockAlertController {
    @Autowired
    private StockAlertService stockAlertService;

    @GetMapping("/minimum")
    public List<Map<String, Object>> getMaterialsBelowMinimumStock() {
        return stockAlertService.getMaterialsBelowMinimumStock();
    }
}
