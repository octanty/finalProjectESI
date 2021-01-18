package com.example.demo.maintenance.application.dto;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.dto.PlantReservationDTO;
import com.example.demo.maintenance.domain.model.TypeOfWork;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@Data
public class MaintenanceTaskDTO extends RepresentationModel<MaintenanceTaskDTO> {
    Long _id;
    String description;
    TypeOfWork typeOfWork;
    BusinessPeriodDTO taskPeriod;
    BigDecimal price;
    PlantReservationDTO reservation;
}

//public class MaintenanceTaskDTO extends ResourceSupport {
//    Long _id;
//    String description;
//    TypeOfWork typeOfWork;
//
//    BusinessPeriodDTO taskPeriod;
//    BigDecimal price;
//    PlantReservationDTO reservation;
//
//    public BusinessPeriodDTO getTaskPeriod() {
//        return taskPeriod;
//    }
//
//    public void setTaskPeriod(BusinessPeriodDTO taskPeriod) {
//        this.taskPeriod = taskPeriod;
//    }
//}
