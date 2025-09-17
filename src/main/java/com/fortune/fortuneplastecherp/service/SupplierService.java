package com.fortune.fortuneplastecherp.service;

import com.fortune.fortuneplastecherp.domain.Supplier;
import com.fortune.fortuneplastecherp.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplierDetails) {
        Optional<Supplier> optionalSupplier = supplierRepository.findById(id);
        if (optionalSupplier.isPresent()) {
            Supplier supplier = optionalSupplier.get();
            supplier.setName(supplierDetails.getName());
            supplier.setGstNumber(supplierDetails.getGstNumber());
            supplier.setContactPerson(supplierDetails.getContactPerson());
            supplier.setContactNumber(supplierDetails.getContactNumber());
            supplier.setEmail(supplierDetails.getEmail());
            supplier.setAddress(supplierDetails.getAddress());
            supplier.setPaymentTerms(supplierDetails.getPaymentTerms());
            return supplierRepository.save(supplier);
        } else {
            throw new RuntimeException("Supplier not found");
        }
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
