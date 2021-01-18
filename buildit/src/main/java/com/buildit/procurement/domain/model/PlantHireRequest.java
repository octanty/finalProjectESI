package com.buildit.procurement.domain.model;

import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.rental.domain.model.ConstructionSite;
import com.buildit.rental.domain.model.PlantInventoryEntry;
import com.buildit.rental.domain.model.PurchaseOrder;
import com.buildit.rental.domain.model.Supplier;
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

    @ManyToOne
    Supplier supplier;

    @ManyToOne
    ConstructionSite constructionSite;

    @ManyToOne
    Employee requestingSiteEngineer;

    @ManyToOne
    Employee approvingWorksEngineer;

//    @OneToOne(cascade = CascadeType.ALL)
//    ExtensionRequest extensionRequest;
}
