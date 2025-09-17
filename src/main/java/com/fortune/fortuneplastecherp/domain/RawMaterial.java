package com.fortune.fortuneplastecherp.domain;

import jakarta.persistence.*;

@Entity
public class RawMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String name;
    private String unit;
    private Double standardCost;
    private String category;
    private String specification;
    private Double minimumStockLevel;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public Double getStandardCost() { return standardCost; }
    public void setStandardCost(Double standardCost) { this.standardCost = standardCost; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public Double getMinimumStockLevel() { return minimumStockLevel; }
    public void setMinimumStockLevel(Double minimumStockLevel) { this.minimumStockLevel = minimumStockLevel; }
}
