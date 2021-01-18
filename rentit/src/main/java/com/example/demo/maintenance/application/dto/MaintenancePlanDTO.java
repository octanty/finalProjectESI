package com.example.demo.maintenance.application.dto;

import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import lombok.Data;

import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

@Data
public class MaintenancePlanDTO extends RepresentationModel<MaintenancePlanDTO> {
    Long _id;
    Integer yearOfAction;
    PlantInventoryItemDTO plant;
}

//public class MaintenancePlanDTO extends ResourceSupport {
//    Long _id;
//    Integer yearOfAction;
//
//    PlantInventoryItemDTO plant;
//
//
//    public Long get_id() {
//        return _id;
//    }
//
//    public void set_id(Long _id) {
//        this._id = _id;
//    }
//    public PlantInventoryItemDTO getPlant() {
//        return plant;
//    }
//
//    public void setPlant(PlantInventoryItemDTO plant) {
//        this.plant = plant;
//    }
//
//    public Integer getYearOfAction() {
//        return yearOfAction;
//    }
//
//    public void setYearOfAction(Integer yearOfAction) {
//        this.yearOfAction = yearOfAction;
//    }
//}
