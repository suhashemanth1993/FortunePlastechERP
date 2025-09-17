package com.fortune.fortuneplastecherp.repository;

import com.fortune.fortuneplastecherp.domain.RawMaterialIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RawMaterialIssueRepository extends JpaRepository<RawMaterialIssue, Long> {
    List<RawMaterialIssue> findByProductionOrderId(String productionOrderId);
}
