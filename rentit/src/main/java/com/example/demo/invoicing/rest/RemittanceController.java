package com.example.demo.invoicing.rest;

import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.application.dto.RemittanceAdviceDTO;
import com.example.demo.invoicing.application.exceptions.InvoiceNotFoundException;
import com.example.demo.invoicing.application.services.InvoiceAssembler;
import com.example.demo.invoicing.application.services.InvoiceService;
import com.example.demo.invoicing.application.services.RemittanceAssembler;
import com.example.demo.invoicing.application.services.RemittanceService;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.model.RemittanceAdvice;
import com.example.demo.invoicing.domain.repository.RemittanceAdviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remittance")
public class RemittanceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    RemittanceService remittanceService;

    @Autowired
    RemittanceAdviceRepository remittanceAdviceRepository;

    @Autowired
    RemittanceAssembler remittanceAssembler;

    @Autowired
    InvoiceAssembler invoiceAssembler;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RemittanceAdviceDTO getById(@PathVariable("id") Long id) {
        return remittanceService.readOne(id);
    }

    @PostMapping("/{id}/create")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<RemittanceAdviceDTO> createRemittance(@PathVariable("id") Long id) throws Exception {
        if (!invoiceService.invoiceExists(id)) {
            throw new InvoiceNotFoundException(id);
        }

        RemittanceAdvice remittanceAdvice = remittanceService.createRemittance(id);
        RemittanceAdviceDTO remittanceAdviceDTO = remittanceAssembler.toModel(remittanceAdvice);

        return ResponseEntity
            .created(remittanceAdviceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(remittanceAdviceDTO);
    }

    @PostMapping("/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InvoiceDTO> acceptRemittance(@PathVariable("id") Long id) throws Exception {
        if (!invoiceService.invoiceExists(id)) {
            throw new InvoiceNotFoundException(id);
        }

        Invoice invoice = remittanceService.acceptRemittance(id);
        InvoiceDTO invoiceDTO = invoiceAssembler.toModel(invoice);

        return ResponseEntity
            .created(invoiceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(invoiceDTO);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(InvoiceNotFoundException ex) {
    }

}
