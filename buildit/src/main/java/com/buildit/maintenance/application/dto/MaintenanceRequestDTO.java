package com.buildit.maintenance.application.dto;

import com.buildit.common.domain.model.BusinessPeriod;
import lombok.Data;

import javax.persistence.Embedded;

@Data
public class MaintenanceRequestDTO {

    private Long plantId;
    private String serialNumber;
    private String issueDescription;
    private BusinessPeriod maintenancePeriod;
    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String nameOfSupplier;

}
