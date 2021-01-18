package com.example.demo.invoicing.application.services;

import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.rest.InvoiceController;
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
        dto.setPurchaseOrderId(invoice.getPurchaseOrderId());
        dto.setDueDate(invoice.getDueDate());
        dto.setDatePayment(invoice.getDatePayment());
        dto.setPayableAmount(invoice.getPayableAmount());
        dto.setRejectionNote(invoice.getRejectionNode());

        return dto;
    }
}
