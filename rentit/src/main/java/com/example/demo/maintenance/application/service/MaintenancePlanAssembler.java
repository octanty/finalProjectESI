package com.example.demo.maintenance.application.service;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.service.PlantInventoryEntryAssembler;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.maintenance.domain.model.MaintenancePlan;
import com.example.demo.maintenance.rest.MaintenanceRestController;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import com.example.demo.sales.domain.model.PurchaseOrder;
import com.example.demo.sales.rest.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

@Service
public class MaintenancePlanAssembler extends RepresentationModelAssemblerSupport<MaintenancePlan, MaintenancePlanDTO> {
    public MaintenancePlanAssembler() {
        super(MaintenanceRestController.class, MaintenancePlanDTO.class);
    }

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @Override
    public MaintenancePlanDTO toModel(MaintenancePlan maintenancePlan) {
        MaintenancePlanDTO dto = createModelWithId(maintenancePlan.getId(), maintenancePlan);
        dto.set_id(maintenancePlan.getId());
        dto.setYearOfAction(maintenancePlan.getYearOfAction());
        dto.setPlant(plantInventoryItemAssembler.toModel(maintenancePlan.getPlant()));

        return dto;
    }
}
/*
public class MaintenancePlanAssembler extends ResourceAssemblerSupport<MaintenancePlan, MaintenancePlanDTO> {
    public MaintenancePlanAssembler(){
        super(MaintenanceRestController.class, MaintenancePlanDTO.class);
    }

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;
    @Override
    public MaintenancePlanDTO toResource(MaintenancePlan maintenancePlan) {
        MaintenancePlanDTO dto = createResourceWithId(maintenancePlan.getId(), maintenancePlan);
        dto.set_id(maintenancePlan.getId());
        dto.setYearOfAction(maintenancePlan.getYearOfAction());
        dto.setPlant(plantInventoryItemAssembler.toResource(maintenancePlan.getPlant()));
        return dto;
    }
}

* */
