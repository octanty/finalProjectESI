package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.RemittanceAdviceDTO;
import com.buildit.procurement.domain.model.RemittanceAdvice;
import com.buildit.procurement.rest.InvoiceController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class RemittanceAdviceAssembler extends RepresentationModelAssemblerSupport<RemittanceAdvice, RemittanceAdviceDTO> {
    public RemittanceAdviceAssembler() {
        super(InvoiceController.class, RemittanceAdviceDTO.class);
    }

    @Override
    public RemittanceAdviceDTO toModel(RemittanceAdvice remittanceAdvice) {
        RemittanceAdviceDTO dto = createModelWithId(remittanceAdvice.getId(), remittanceAdvice);
        dto.set_id(remittanceAdvice.getId());
        dto.setNote(remittanceAdvice.getNote());
        return dto;
    }
}
