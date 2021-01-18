package com.buildit.rental.application.dto;

import com.buildit.common.domain.model.BusinessPeriod;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MaintenanceOrderDTO extends RepresentationModel<MaintenanceOrderDTO> {
    private Long _id;
    private MStatus status;
    private String issueDescription;
    private BusinessPeriod maintenancePeriod;
    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String nameOfSupplier;
    private PlantInventoryItemDTO plant;

//    private List<Link> links;
}
