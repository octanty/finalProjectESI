package com.example.demo.invoicing.application.services;

import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.application.dto.RemittanceAdviceDTO;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.model.InvoiceStatus;
import com.example.demo.invoicing.domain.model.RemittanceAdvice;
import com.example.demo.invoicing.domain.repository.InvoiceRepository;
import com.example.demo.invoicing.domain.repository.RemittanceAdviceRepository;
import com.example.demo.sales.application.services.SalesService;
import com.example.demo.sales.domain.model.PurchaseOrder;
import com.example.demo.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class RemittanceService {
    @Value("http://localhost:9000")
    String buildITUrl;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RemittanceAdviceRepository remittanceAdviceRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    RemittanceAssembler remittanceAssembler;

    @Autowired
    SalesService salesService;

    public RemittanceAdviceDTO readOne(Long id) {
        Optional<RemittanceAdvice> maybeInvoice = remittanceAdviceRepository.findById(id);

        if (!maybeInvoice.isPresent()) {
            throw new IllegalArgumentException("No invoice found with id = " + id);
        }

        return remittanceAssembler.toModel(maybeInvoice.get());
    }

    public RemittanceAdvice createRemittance(Long invoiceId) {
        Invoice invoice = findInvoice(invoiceId);
        RemittanceAdvice remittanceAdvice = new RemittanceAdvice();
        remittanceAdvice.setInvoice(invoice);
        remittanceAdvice.setNote("test");

        return remittanceAdviceRepository.save(remittanceAdvice);
    }

    public Invoice acceptRemittance(Long invoiceId) {
        Invoice invoice = findInvoice(invoiceId);
        invoice.setStatus(InvoiceStatus.PAID);

        return invoiceRepository.save(invoice);
    }


    public Invoice findInvoice(Long id) {
        return invoiceRepository.getOne(id);
    }
}
