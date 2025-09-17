package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Supplier;
import com.fortune.fortuneplastecherp.repository.SupplierRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierServiceTest {
    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveSupplier() {
        Supplier supplier = new Supplier();
        supplier.setName("Test Supplier");
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        Supplier saved = supplierService.saveSupplier(supplier);
        assertEquals("Test Supplier", saved.getName());
    }

    @Test
    void testGetSupplierById() {
        Supplier supplier = new Supplier();
        supplier.setId(1L);
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        Optional<Supplier> found = supplierService.getSupplierById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testDeleteSupplier() {
        doNothing().when(supplierRepository).deleteById(1L);
        supplierService.deleteSupplier(1L);
        verify(supplierRepository, times(1)).deleteById(1L);
    }
}
