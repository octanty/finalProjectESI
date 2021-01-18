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

import java.time.LocalDate;
import java.util.Optional;

@Service
public class InvoiceService {
    @Value("http://localhost:9000")
    String buildITUrl;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RemittanceAdviceRepository remittanceAdviceRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    InvoiceAssembler invoiceAssembler;

    @Autowired
    SalesService salesService;

    public InvoiceDTO readOne(Long id) {
        Optional<Invoice> maybeInvoice = invoiceRepository.findById(id);

        if (!maybeInvoice.isPresent()) {
            throw new IllegalArgumentException("No invoice found with id = " + id);
        }

        return invoiceAssembler.toModel(maybeInvoice.get());
    }

    public Invoice findInvoice(Long id) {
        return invoiceRepository.getOne(id);
    }

    public Boolean invoiceExists(Long id) {
        return invoiceRepository.existsById(id);
    }

    public Invoice rejectInvoice(Long id) {
        Invoice invoice = findInvoice(id);
        invoice.setStatus(InvoiceStatus.REJECTED_BY_CUSTOMER);

        return invoiceRepository.save(invoice);
    }

    public Invoice confirmInvoice(Long id) {
        Invoice invoice = findInvoice(id);
        invoice.setStatus(InvoiceStatus.PAID);

        return invoiceRepository.save(invoice);

    }

    public Invoice acceptInvoice(Long invoiceId, RemittanceAdviceDTO dto) {
        Invoice invoice = findInvoice(invoiceId);
        RemittanceAdvice remittanceAdvice = new RemittanceAdvice();
        remittanceAdvice.setInvoice(invoice);
        remittanceAdvice.setNote(dto.getNote());

        invoice.setRemittanceAdvice(
            remittanceAdviceRepository.save(remittanceAdvice));

        return invoiceRepository.save(invoice);
    }

    public void createInvoice(Long purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.getOne(purchaseOrderId);
        Invoice invoice = new Invoice();
        invoice.setPurchaseOrderId(purchaseOrder.getId());
        invoice.setDueDate(LocalDate.now().plusWeeks(2));
        invoice.setPayableAmount(purchaseOrder.getTotal());
        invoice.setStatus(InvoiceStatus.PENDING);

        InvoiceDTO invoiceDTO = invoiceAssembler.toModel(invoiceRepository.save(invoice));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        HttpEntity<InvoiceDTO> entity = new HttpEntity<>(invoiceDTO, headers);
        RestTemplate restTemplate = new RestTemplate();


        restTemplate.exchange(
            buildITUrl + "/api/suppliers/invoice/submit",
//            buildITUrl + "/callbacks/submitInvoice",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<String>() {});
    }

    public void submitInvoiceForReturnedPlant(Long purchaseOrderId) throws Exception {
        salesService.markAsReturned(purchaseOrderId);
        createInvoice(purchaseOrderId);
    }
}
