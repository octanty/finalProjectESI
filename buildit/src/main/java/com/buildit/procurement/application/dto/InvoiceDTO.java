package com.buildit.procurement.application.dto;

import com.buildit.procurement.domain.model.InvoiceStatus;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class InvoiceDTO extends RepresentationModel<InvoiceDTO> {
    Long _id;
    //Long purchaseOrderId;
    InvoiceStatus status;
    LocalDate dueDate;
    LocalDate datePayment;
    Boolean latePayment;
    BigDecimal payableAmount;
    String rejectionNote;
    PurchaseOrderDTO purchaseOrder;

}
