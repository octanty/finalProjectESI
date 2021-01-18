package com.example.demo.inventory.application.service;

import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.inventory.rest.InventoryRestController;
import com.example.demo.sales.rest.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

@Service
public class PlantInventoryItemAssembler extends RepresentationModelAssemblerSupport<PlantInventoryItem, PlantInventoryItemDTO> {
    public PlantInventoryItemAssembler() {
        super(SalesRestController.class, PlantInventoryItemDTO.class);
    }
//    public PlantInventoryItemAssembler() {
//        super(InventoryRestController.class, PlantInventoryItemDTO.class);
//    }

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Override
    public PlantInventoryItemDTO toModel(PlantInventoryItem plantInventoryItem) {
        PlantInventoryItemDTO dto = createModelWithId(plantInventoryItem.getId(), plantInventoryItem);
        dto.set_id(plantInventoryItem.getId());
        dto.setSerialNumber(plantInventoryItem.getSerialNumber());
        dto.setEquipmentCondition(plantInventoryItem.getEquipmentCondition());
        dto.setPlantInfo(plantInventoryEntryAssembler.toModel(plantInventoryItem.getPlantInfo()));

        return dto;
    }
}
