package com.fortune.fortuneplastecherp.domain;

import jakarta.persistence.*;

@Entity
public class RawMaterialIssueBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id")
    private RawMaterialIssue issue;

    @ManyToOne
    @JoinColumn(name = "batch_id")
    private Batch batch;

    private Double quantityIssued;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RawMaterialIssue getIssue() { return issue; }
    public void setIssue(RawMaterialIssue issue) { this.issue = issue; }
    public Batch getBatch() { return batch; }
    public void setBatch(Batch batch) { this.batch = batch; }
    public Double getQuantityIssued() { return quantityIssued; }
    public void setQuantityIssued(Double quantityIssued) { this.quantityIssued = quantityIssued; }
}
