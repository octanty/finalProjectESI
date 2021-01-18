package com.buildit.procurement.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PlantSupplierInvoiceDTO {
    Long _id;
    Long purchaseOrderId;
    LocalDate dueDate;
    BigDecimal payableAmount;
    //    TODO: Map LINKS
}
