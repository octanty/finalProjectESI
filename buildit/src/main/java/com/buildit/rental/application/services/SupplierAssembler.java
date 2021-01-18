package com.buildit.rental.application.services;

import com.buildit.procurement.rest.ProcurementRestController;
import com.buildit.rental.application.dto.SupplierDTO;
import com.buildit.rental.domain.model.Supplier;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class SupplierAssembler extends RepresentationModelAssemblerSupport<Supplier, SupplierDTO> {
    public SupplierAssembler() {
        super(ProcurementRestController.class, SupplierDTO.class);
    }

    @Override
    public SupplierDTO toModel(Supplier supplier) {
        SupplierDTO dto = createModelWithId(supplier.getId(), supplier);
        dto.set_id(supplier.getId());
        dto.setName(supplier.getName());
        return dto;
    }
}
