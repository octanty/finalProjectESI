package com.example.demo.inventory.application.dto;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;


@Data
public class PlantReservationDTO extends RepresentationModel<PlantReservationDTO> {
    Long _id;
    BusinessPeriodDTO schedule;
    PurchaseOrderDTO rental;
    PlantInventoryItemDTO plant;
    MaintenancePlanDTO maintPlan;
}
