package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Batch;
import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.domain.Supplier;
import com.fortune.fortuneplastecherp.repository.BatchRepository;
import com.fortune.fortuneplastecherp.repository.RawMaterialRepository;
import com.fortune.fortuneplastecherp.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BatchServiceTest {
    @Mock
    private BatchRepository batchRepository;
    @Mock
    private RawMaterialRepository rawMaterialRepository;
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private BatchService batchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceiveBatch() {
        RawMaterial rm = new RawMaterial();
        rm.setId(1L);
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rm));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(batchRepository.save(any(Batch.class))).thenAnswer(i -> i.getArgument(0));
        Batch batch = batchService.receiveBatch(1L, 1L, 100.0, 50.0, LocalDate.now());
        assertEquals(rm, batch.getRawMaterial());
        assertEquals(supplier, batch.getSupplier());
        assertEquals(100.0, batch.getQuantity());
        assertEquals(50.0, batch.getCostPerUnit());
    }
}
