package com.buildit.rental.application.services;

import com.buildit.rental.application.dto.SupplierDTO;
import com.buildit.rental.domain.model.Supplier;
import com.buildit.rental.domain.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    SupplierRepository supplierRepository;

    @Autowired
    SupplierAssembler supplierAssembler;

    public Supplier create(String name) {
        Supplier supplier = new Supplier();
        supplier.setName(name);

        return supplierRepository.save(supplier);
    }

    public Supplier readModel(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);

        if (supplier == null) {
            throw new IllegalArgumentException("Cannot find supplier with id: " + id);
        }

        return supplier;
    }

    public CollectionModel<SupplierDTO> readAll() {
        return supplierAssembler.toCollectionModel(supplierRepository.findAll());
    }

    public SupplierDTO readOne(Long id) {
        return supplierAssembler.toModel(readModel(id));
    }

    // public Supplier getFirstAsModel()
    // public SupplierDTO findOrCreateByName()
}
