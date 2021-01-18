package com.buildit.procurement.rest;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.application.dto.PlantSupplierInvoiceDTO;
import com.buildit.procurement.application.dto.PlantSupplierPOUpdateDTO;
import com.buildit.procurement.application.service.InvoiceService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    @Autowired
    InvoiceService invoiceService;

    @PostMapping("/invoice/submit")
    public boolean submitInvoice(@RequestBody PlantSupplierInvoiceDTO po) {
        InvoiceDTO invoice = invoiceService.add(po);
        return !Objects.isNull(invoice);
    }
}
