package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.rest.InvoiceController;
import com.buildit.rental.application.services.PurchaseOrderAssembler;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAssembler extends RepresentationModelAssemblerSupport<Invoice, InvoiceDTO> {
    public InvoiceAssembler() {
        super(InvoiceController.class, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO toModel(Invoice invoice) {
        InvoiceDTO dto = createModelWithId(invoice.getId(), invoice);
        dto.set_id(invoice.getId());
        dto.setDueDate(invoice.getDueDate());
        dto.setDatePayment(invoice.getDatePayment());
        dto.setLatePayment(invoice.getLatePayment());
        dto.setStatus(invoice.getStatus());
        dto.setPayableAmount(invoice.getPayableAmount());
        PurchaseOrderAssembler poAssembler = new PurchaseOrderAssembler();
        dto.setPurchaseOrder(poAssembler.toModel(invoice.getPurchaseOrder()));
        //dto.setPurchaseOrderId(invoice.getPurchaseOrderId());

        return dto;
    }
}
