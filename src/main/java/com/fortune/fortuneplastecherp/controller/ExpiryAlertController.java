package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.service.ExpiryAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expiry-alerts")
public class ExpiryAlertController {
    @Autowired
    private ExpiryAlertService expiryAlertService;

    @GetMapping
    public List<Map<String, Object>> getExpiredOrNearExpiryBatches(@RequestParam(defaultValue = "30") int daysThreshold) {
        return expiryAlertService.getExpiredOrNearExpiryBatches(daysThreshold);
    }
}
