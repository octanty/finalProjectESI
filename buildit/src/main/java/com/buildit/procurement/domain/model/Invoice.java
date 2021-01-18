package com.buildit.procurement.domain.model;

import com.buildit.rental.domain.model.PurchaseOrder;
import lombok.Data;
import org.apache.xpath.operations.Bool;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Invoice {
    @Id
    @GeneratedValue
    Long id;

    //@Column(nullable = false)
    //Long purchaseOrderId;

    @OneToOne
    PurchaseOrder purchaseOrder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InvoiceStatus status;

    LocalDate dueDate;
    Boolean latePayment;
    LocalDate datePayment;

    @OneToOne
    RemittanceAdvice remittanceAdvice;

    @Column(precision = 8, scale = 2)
    BigDecimal payableAmount;

    String rejectionNote;

}
