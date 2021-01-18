package com.example.demo.inventory.application.dto;

import com.example.demo.inventory.domain.model.EquipmentCondition;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

@Data
public class PlantInventoryItemDTO extends RepresentationModel<PlantInventoryItemDTO> {
    Long _id;
    String serialNumber;
    EquipmentCondition equipmentCondition;
    PlantInventoryEntryDTO plantInfo;
}

//@Data
//public class PlantInvItemDTO extends ResourceSupport {
//    Long _id;
//    String serialNumber;
//    EquipmentCondition equipmentCondition;
//    PlantInventoryEntryDTO plantInfo;
//
//
//
//    public PlantInventoryEntryDTO getPlantInfo() {
//        return plantInfo;
//    }
//
//    public void setPlantInfo(PlantInventoryEntryDTO plant) {
//        this.plantInfo = plant;
//    }
//
//    public Long get_id() {
//        return _id;
//    }
//
//    public void set_id(Long _id) {
//        this._id = _id;
//    }
//
//    public String getSerialNumber() {
//        return serialNumber;
//    }
//
//    public void setSerialNumber(String serialNumber) {
//        this.serialNumber = serialNumber;
//    }
//
//    public EquipmentCondition getEquipmentCondition(){
//        return equipmentCondition;
//    }
//
//    public void setEquipmentCondition(EquipmentCondition equipmentCondition){
//        this.equipmentCondition = equipmentCondition;
//
//    }
//}
