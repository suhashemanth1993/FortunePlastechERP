package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.repository.RawMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RawMaterialService {
    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    public RawMaterial saveRawMaterial(RawMaterial rawMaterial) {
        return rawMaterialRepository.save(rawMaterial);
    }

    public RawMaterial updateRawMaterial(Long id, RawMaterial details) {
        Optional<RawMaterial> optional = rawMaterialRepository.findById(id);
        if (optional.isPresent()) {
            RawMaterial rm = optional.get();
            rm.setCode(details.getCode());
            rm.setName(details.getName());
            rm.setUnit(details.getUnit());
            rm.setStandardCost(details.getStandardCost());
            rm.setCategory(details.getCategory());
            rm.setSpecification(details.getSpecification());
            return rawMaterialRepository.save(rm);
        } else {
            throw new RuntimeException("Raw Material not found");
        }
    }

    public List<RawMaterial> getAllRawMaterials() {
        return rawMaterialRepository.findAll();
    }

    public Optional<RawMaterial> getRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id);
    }

    public void deleteRawMaterial(Long id) {
        rawMaterialRepository.deleteById(id);
    }
}
