package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.BinDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bin-dashboard")
public class BinDashboardController {
    @Autowired
    private BinDashboardService binDashboardService;

    @GetMapping
    public List<Map<String, Object>> getStockByMaterialBatch(
            @RequestParam(required = false) Long rawMaterialId,
            @RequestParam(required = false) String location
    ) {
        return binDashboardService.getStockByMaterialBatch(rawMaterialId, location);
    }
}
