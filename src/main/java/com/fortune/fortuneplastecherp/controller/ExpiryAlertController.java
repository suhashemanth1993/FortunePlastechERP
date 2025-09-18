package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.ExpiryAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expiry-alerts")
@CrossOrigin(origins = "http://localhost:3002")
public class ExpiryAlertController {
    @Autowired
    private ExpiryAlertService expiryAlertService;

    @GetMapping
    public List<Map<String, Object>> getExpiredOrNearExpiryBatches(@RequestParam(defaultValue = "30") int daysThreshold) {
        return expiryAlertService.getExpiredOrNearExpiryBatches(daysThreshold);
    }
}
