package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.BinDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bin-dashboard")
@CrossOrigin(origins = "http://localhost:3002")
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
