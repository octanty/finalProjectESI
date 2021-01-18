package com.buildit.rental.application.dto;

import com.buildit.rental.domain.model.EquipmentCondition;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@Data
public class PlantInventoryItemDTO extends RepresentationModel<PlantInventoryItemDTO> {
    Long _id;
    String serialNumber;
    EquipmentCondition equipmentCondition;
    PlantInventoryEntryDTO plantInfo;

}
