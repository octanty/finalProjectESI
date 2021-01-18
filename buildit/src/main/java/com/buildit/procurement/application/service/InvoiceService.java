package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.domain.model.InvoiceStatus;
import com.buildit.procurement.domain.repositories.InvoiceRepository;
import com.buildit.rental.application.services.RentalService;
import com.buildit.rental.domain.model.POStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import com.buildit.procurement.application.dto.PlantSupplierInvoiceDTO;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.domain.model.InvoiceStatus;
import com.buildit.procurement.domain.repositories.InvoiceRepository;
import com.buildit.rental.application.services.PurchaseOrderService;
import com.buildit.rental.domain.model.PurchaseOrder;
import com.buildit.rental.domain.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    private static final String BASE_URL = "http://localhost:8080/api";

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoiceAssembler invoiceAssembler;

    @Autowired
    RentalService rentalService;

    @Autowired
    PurchaseOrderService purchaseOrderService;


    @Autowired
    RemittanceAdviceService remittanceAdviceService;

    public InvoiceDTO add(PlantSupplierInvoiceDTO supplierInvoice) {
        PurchaseOrder po = purchaseOrderService.findByExternalId(supplierInvoice.getPurchaseOrderId());
        Invoice invoice = new Invoice();
        invoice.setPurchaseOrder(po);
        //invoice.setPurchaseOrderId(supplierInvoice.getPurchaseOrderId())
        invoice.setDueDate(supplierInvoice.getDueDate());
        invoice.setLatePayment(false);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setPayableAmount(supplierInvoice.getPayableAmount());
        invoice = invoiceRepository.save(invoice);
        return invoiceAssembler.toModel(invoice);
    }


    public Invoice findInvoice(Long id) {
        return invoiceRepository.getOne(id);
    }


    public InvoiceDTO accept(Long invoiceId) throws Exception {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            throw new IllegalArgumentException("Cannot find invoice with id = " + invoiceId);
        }

        if (invoice.getStatus() == InvoiceStatus.PENDING) {
            remittanceAdviceService.createRemittance(invoiceId);
            invoice.setStatus(InvoiceStatus.ACCEPTED);
            invoice = invoiceRepository.save(invoice);
        } else {
            throw new IllegalStateException("Only pending invoices can be accepted");
        }

        POStatus poStatus = rentalService.checkStatusPO(invoice.getPurchaseOrder().getId());
        if (poStatus.equals(POStatus.PENDING)) {
            remittanceAdviceService.create(invoiceId, "Invoice accepted");
            invoice.setStatus(InvoiceStatus.ACCEPTED);
            invoice.setDueDate(LocalDate.now().plusWeeks(2));
            invoice.setDatePayment(LocalDate.now());
            //InvoiceDTO invoiceDTO = new InvoiceDTO();
            //invoiceDTO = restTemplate.postForObject(BASE_URL + "/invoices/{id}/accept", invoiceId, InvoiceDTO.class);
        }
        else {
            invoice.setStatus(InvoiceStatus.REJECTED);
            throw new IllegalStateException("Only pending invoices can be accepted");
        }
        return invoiceAssembler.toModel(invoice);
    }

    public InvoiceDTO acceptRentIT(Long invoiceId, Long supplierId) throws Exception {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            throw new IllegalArgumentException("Cannot find invoice with id = " + invoiceId);
        }

        if (invoice.getStatus() == InvoiceStatus.PENDING) {
            remittanceAdviceService.createRemittanceRentIT(invoiceId, supplierId);
            invoice.setStatus(InvoiceStatus.ACCEPTED);
            invoice = invoiceRepository.save(invoice);
        } else {
            throw new IllegalStateException("Only pending invoices can be accepted");
        }

        POStatus poStatus = rentalService.checkStatusPO(invoice.getPurchaseOrder().getId());
        if (poStatus.equals(POStatus.PENDING)) {
            remittanceAdviceService.create(invoiceId, "Invoice accepted");
            invoice.setStatus(InvoiceStatus.ACCEPTED);
            invoice.setDueDate(LocalDate.now().plusWeeks(2));
            invoice.setDatePayment(LocalDate.now());
            //InvoiceDTO invoiceDTO = new InvoiceDTO();
            //invoiceDTO = restTemplate.postForObject(BASE_URL + "/invoices/{id}/accept", invoiceId, InvoiceDTO.class);
        }
        else {
            invoice.setStatus(InvoiceStatus.REJECTED);
            throw new IllegalStateException("Only pending invoices can be accepted");
        }
        return invoiceAssembler.toModel(invoice);
    }

    public CollectionModel<InvoiceDTO> readAll() {
        return invoiceAssembler.toCollectionModel(invoiceRepository.findAll());
    }

    public InvoiceDTO readOne(Long id) {
        return invoiceAssembler.toModel(readModel(id));
    }

    public Invoice readModel(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);
        if (invoice == null) {
            throw new IllegalStateException("No invoice with id = " + id);
        }

        return invoice;
    }


}
