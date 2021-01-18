package com.example.demo.invoicing.rest;

import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.application.dto.RemittanceAdviceDTO;
import com.example.demo.invoicing.application.exceptions.InvoiceNotFoundException;
import com.example.demo.invoicing.application.services.InvoiceAssembler;
import com.example.demo.invoicing.application.services.InvoiceService;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceAssembler invoiceAssembler;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public InvoiceDTO getById(@PathVariable("id") Long id) {
        return invoiceService.readOne(id);
    }

    @PostMapping("/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InvoiceDTO> rejectInvoice(@PathVariable("id") Long id, @RequestBody InvoiceDTO dto) throws Exception {
        if (!invoiceService.invoiceExists(id)) {
            throw new InvoiceNotFoundException(id);
        }

        Invoice invoice = invoiceService.rejectInvoice(id);
        InvoiceDTO invoiceDTO = invoiceAssembler.toModel(invoice);

        return ResponseEntity.ok(invoiceDTO);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(invoiceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        return ResponseEntity
//            .created(invoiceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri())
//            .body(invoiceDTO);
    }

    @PostMapping("/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InvoiceDTO> acceptInvoice(@PathVariable("id") Long id, @RequestBody RemittanceAdviceDTO remittanceAdviceDTO) throws Exception {
        if (!invoiceService.invoiceExists(id)) {
            throw new InvoiceNotFoundException(id);
        }

        Invoice invoice = invoiceService.acceptInvoice(id, remittanceAdviceDTO);
        InvoiceDTO invoiceDTO = invoiceAssembler.toModel(invoice);

        return ResponseEntity
            .created(invoiceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(invoiceDTO);

//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(invoiceDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        return new ResponseEntity<>(invoiceDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> confirmInvoice(@PathVariable("id") Long id) throws  Exception {
        if (!invoiceService.invoiceExists(id)) {
            throw new InvoiceNotFoundException(id);
        }

        Invoice invoice = invoiceService.confirmInvoice(id);
        InvoiceDTO invoiceDTO = invoiceAssembler.toModel(invoice);

        return ResponseEntity.ok(invoiceDTO);
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(InvoiceNotFoundException ex) {
    }

    @PostMapping("/{id}/submitReturnPlant")
    @ResponseStatus(HttpStatus.OK)
    public void submitInvoiceReturnPlant(@PathVariable("id") Long purchaseOrderID) throws Exception {
        invoiceService.submitInvoiceForReturnedPlant(purchaseOrderID);
    }
}
