package com.fortune.fortuneplastecherp.repository;

import com.fortune.fortuneplastecherp.domain.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long> {
    Batch findByBatchId(String batchId);
}
