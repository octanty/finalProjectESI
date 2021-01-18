package com.example.demo.inventory.application.service;

import com.example.demo.inventory.application.dto.PlantInventoryEntryAvailabilityDTO;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.sales.rest.SalesRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlantInventoryEntryAvailabilityAssembler extends RepresentationModelAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryAvailabilityDTO> {
    public PlantInventoryEntryAvailabilityAssembler() {
        super(SalesRestController.class, PlantInventoryEntryAvailabilityDTO.class);
    }

    @Override
    public PlantInventoryEntryAvailabilityDTO toModel(PlantInventoryEntry plantInventoryEntry) {
        PlantInventoryEntryAvailabilityDTO dto = createModelWithId(plantInventoryEntry.getId(), plantInventoryEntry);
        dto.set_id(plantInventoryEntry.getId());
        dto.setName(plantInventoryEntry.getName());
        dto.setPrice(plantInventoryEntry.getPrice());
        dto.setDescription(plantInventoryEntry.getDescription());
        dto.setAvailable(false);
        dto.setTotalPrice(BigDecimal.ZERO);

        return dto;
    }
}
