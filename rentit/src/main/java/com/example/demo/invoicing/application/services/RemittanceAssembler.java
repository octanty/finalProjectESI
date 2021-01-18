package com.example.demo.invoicing.application.services;

import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.application.dto.RemittanceAdviceDTO;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.model.RemittanceAdvice;
import com.example.demo.invoicing.rest.InvoiceController;
import com.example.demo.invoicing.rest.RemittanceController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class RemittanceAssembler extends RepresentationModelAssemblerSupport<RemittanceAdvice, RemittanceAdviceDTO> {
    public RemittanceAssembler() {
        super(RemittanceController.class, RemittanceAdviceDTO.class);
    }

    @Override
    public RemittanceAdviceDTO toModel(RemittanceAdvice remittanceAdvice) {
        RemittanceAdviceDTO dto = createModelWithId(remittanceAdvice.getId(), remittanceAdvice);
        dto.set_id(remittanceAdvice.getId());

        return dto;
    }
}
