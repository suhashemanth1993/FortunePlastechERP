package com.fortune.fortuneplastecherp.controller;

import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.service.RawMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/raw-materials")
public class RawMaterialController {
    @Autowired
    private RawMaterialService rawMaterialService;

    @PostMapping
    public ResponseEntity<RawMaterial> createRawMaterial(@RequestBody RawMaterial rawMaterial) {
        RawMaterial saved = rawMaterialService.saveRawMaterial(rawMaterial);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RawMaterial> updateRawMaterial(@PathVariable Long id, @RequestBody RawMaterial rawMaterial) {
        RawMaterial updated = rawMaterialService.updateRawMaterial(id, rawMaterial);
        return ResponseEntity.ok(updated);
    }

    @GetMapping
    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialService.getAllRawMaterials();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RawMaterial> getRawMaterialById(@PathVariable Long id) {
        Optional<RawMaterial> rawMaterial = rawMaterialService.getRawMaterialById(id);
        return rawMaterial.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable Long id) {
        rawMaterialService.deleteRawMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
