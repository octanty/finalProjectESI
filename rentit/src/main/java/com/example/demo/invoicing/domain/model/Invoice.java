package com.example.demo.invoicing.domain.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue
    Long id;

    @Column(nullable = false)
    Long purchaseOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InvoiceStatus status;

    LocalDate dueDate;
    LocalDate datePayment;

    @OneToOne
    RemittanceAdvice remittanceAdvice;

    @Column(precision = 8, scale = 2)
    BigDecimal payableAmount;

    String rejectionNode;
}
