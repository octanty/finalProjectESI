package com.example.demo.maintenance.domain.model;

import com.example.demo.inventory.domain.model.BusinessPeriod;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PUBLIC)

public class MaintenanceOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String issueDescription;
    private BusinessPeriod maintenancePeriod;
    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String nameOfSupplier;

    @Enumerated(EnumType.STRING)
    private MOStatus status;

    @OneToOne
    private PlantInventoryItem plant;
}
