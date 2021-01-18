package com.example.demo.procurement.domain.model;

import com.example.demo.inventory.domain.model.BusinessPeriod;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.sales.domain.model.PurchaseOrder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class PlantHireRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private BusinessPeriod rentalPeriod;

    @Enumerated(EnumType.STRING)
    private PHRStatus status;

    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String comment;

    @Column(precision = 8, scale = 2)
    private BigDecimal totalCost;

    @OneToOne
    private PlantInventoryEntry entry;

    @OneToOne
    private PurchaseOrder po;

}
