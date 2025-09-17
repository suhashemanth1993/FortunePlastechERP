package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.RawMaterial;
import com.fortune.fortuneplastecherp.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RawMaterialServiceTest {
    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private RawMaterialService rawMaterialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveRawMaterial() {
        RawMaterial rm = new RawMaterial();
        rm.setName("Polymer");
        when(rawMaterialRepository.save(any(RawMaterial.class))).thenReturn(rm);
        RawMaterial saved = rawMaterialService.saveRawMaterial(rm);
        assertEquals("Polymer", saved.getName());
    }

    @Test
    void testGetRawMaterialById() {
        RawMaterial rm = new RawMaterial();
        rm.setId(1L);
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rm));
        Optional<RawMaterial> found = rawMaterialService.getRawMaterialById(1L);
        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
    }

    @Test
    void testDeleteRawMaterial() {
        doNothing().when(rawMaterialRepository).deleteById(1L);
        rawMaterialService.deleteRawMaterial(1L);
        verify(rawMaterialRepository, times(1)).deleteById(1L);
    }
}
