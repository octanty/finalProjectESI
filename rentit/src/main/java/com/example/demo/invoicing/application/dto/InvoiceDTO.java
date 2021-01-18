package com.example.demo.invoicing.application.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceDTO extends RepresentationModel<InvoiceDTO> {
    Long _id;
    Long purchaseOrderId;
    LocalDate dueDate;
    LocalDate datePayment;
    BigDecimal payableAmount;
    String rejectionNote;
}
