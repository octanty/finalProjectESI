package com.buildit.procurement.rest;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.application.service.InvoiceAssembler;
import com.buildit.procurement.application.service.InvoiceService;
import com.buildit.procurement.application.service.PlantHireRequestAssembler;

import com.buildit.procurement.domain.model.Invoice;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;


    @Autowired
    InvoiceAssembler invoiceAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<InvoiceDTO>> readAll() {
        CollectionModel<InvoiceDTO> invoices = invoiceService.readAll();
//        invoices.forEach();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> readOne(@PathVariable("id") Long id) {
        InvoiceDTO invoiceDTO = invoiceService.readOne(id);

        return ResponseEntity.ok(invoiceDTO);
    }

    @PostMapping("/{id}/accept")
    public void accept(@PathVariable("id") Long invoiceId) throws Exception {
        invoiceService.accept(invoiceId);
    }
}
