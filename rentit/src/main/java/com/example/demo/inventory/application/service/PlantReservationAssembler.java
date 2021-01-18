package com.example.demo.inventory.application.service;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.domain.model.PlantReservation;
import com.example.demo.inventory.rest.InventoryRestController;
import com.example.demo.inventory.application.dto.PlantReservationDTO;
import com.example.demo.maintenance.application.service.MaintenancePlanAssembler;
import com.example.demo.sales.application.services.PurchaseOrderAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

@Service
public class PlantReservationAssembler extends RepresentationModelAssemblerSupport<PlantReservation, PlantReservationDTO> {
    public PlantReservationAssembler() {
        super(InventoryRestController.class, PlantReservationDTO.class);
    }

    @Autowired
    MaintenancePlanAssembler maintenancePlanAssembler;

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Override
    public PlantReservationDTO toModel(PlantReservation plantReservation) {
        PlantReservationDTO dto = createModelWithId(plantReservation.getId(), plantReservation);
        dto.set_id(plantReservation.getId());
        dto.setSchedule(BusinessPeriodDTO.of(plantReservation.getSchedule().getStartDate(), plantReservation.getSchedule().getEndDate()));
        dto.setMaintPlan(maintenancePlanAssembler.toModel(plantReservation.getMaintPlan()));
        dto.setPlant(plantInventoryItemAssembler.toModel(plantReservation.getPlant()));
        dto.setRental(purchaseOrderAssembler.toModel(plantReservation.getRental()));

        return dto;
    }
}

//public class PlantReservationAssembler extends ResourceAssemblerSupport<PlantReservation, PlantReservationDTO> {
//    public PlantReservationAssembler()
//    {
//        super(InventoryRestController.class, PlantReservationDTO.class);
//    }
//    @Autowired
//    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
//
//    @Autowired
//    PlantInventoryItemAssembler plantInventoryItemAssembler;
//
//    @Autowired
//    MaintenancePlanAssembler maintenancePlanAssembler;
//
//    @Autowired
//    com.example.demo.sales.application.services.PurchaseOrderAssembler purchaseOrderAssembler;
//
//    @Override
//    public PlantReservationDTO toResource(PlantReservation plantReservation) {
//        PlantReservationDTO dto = createResourceWithId(plantReservation.getId(), plantReservation);
//        dto.set_id(plantReservation.getId());
//        dto.setSchedule( BusinessPeriodDTO.of(plantReservation.getSchedule().getStartDate(), plantReservation.getSchedule().getEndDate()));
//        dto.setMaint_plan(maintenancePlanAssembler.toResource(plantReservation.getMaint_plan()));
//        dto.setPlant(plantInventoryItemAssembler.toResource(plantReservation.getPlant()));
//        dto.setRental(purchaseOrderAssembler.toResource(plantReservation.getRental()));
//
//
//
//        return dto;
//    }
//}
