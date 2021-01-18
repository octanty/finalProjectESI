package com.example.demo.inventory.application.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
public class PlantInventoryEntryAvailabilityDTO extends RepresentationModel<PlantInventoryEntryAvailabilityDTO> {
    Long _id;
    String name;
    String description;
    BigDecimal price;
    Boolean available;
    BigDecimal totalPrice;
}
