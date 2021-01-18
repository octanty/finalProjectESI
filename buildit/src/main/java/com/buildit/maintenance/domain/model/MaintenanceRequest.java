package com.buildit.maintenance.domain.model;

import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.rental.domain.model.MaintenanceOrder;
import com.buildit.rental.domain.model.PlantInventoryItem;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MaintenanceRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueDescription;

    @Embedded
    private BusinessPeriod maintenancePeriod;

    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String nameOfSupplier;

    @OneToOne
    private PlantInventoryItem plant;

    @OneToOne
    private MaintenanceOrder mo;

}
