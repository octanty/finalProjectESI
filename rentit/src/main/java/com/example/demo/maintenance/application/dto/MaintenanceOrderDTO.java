package com.example.demo.maintenance.application.dto;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.domain.model.BusinessPeriod;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.maintenance.domain.model.MOStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

import javax.persistence.OneToOne;

@Data
@EqualsAndHashCode(callSuper = false)
public class MaintenanceOrderDTO extends RepresentationModel<MaintenanceOrderDTO> {
    Long _id;
    String issueDescription;
    BusinessPeriodDTO maintenancePeriod;
    String nameOfSiteEngineer;
    String nameOfConstructionSite;
    String nameOfSupplier;
    MOStatus status;
    PlantInventoryItemDTO plant;
}

//@Data
//public class MaintenanceOrderDTO extends ResourceSupport {
//
//    Long _id;
//
//    String issueDescription;
//    BusinessPeriod maintenancePeriod;
//    String nameOfSiteEngineer;
//    String nameOfConstructionSite;
//    String nameOfSupplier;
//
//    MOStatus status;
//
//    @OneToOne
//    private PlantInventoryItemDTO plant;
//
//    // public BusinessPeriodDTO getTaskPeriod() {
//    //     return taskPeriod;
//    // }
//    //
//    // public void setTaskPeriod(BusinessPeriodDTO taskPeriod) {
//    //     this.taskPeriod = taskPeriod;
//    // }
//}
