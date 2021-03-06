package com.example.demo.procurement.application.dto;
import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.procurement.domain.model.PHRStatus;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

@Data
public class PlantHireRequestDTO extends RepresentationModel<PlantHireRequestDTO> {
    private Long _id;
    private PHRStatus status;
    private Long entryId;
    private String entryName;
    private String nameOfSiteEngineer;
    private String nameOfConstructionSite;
    private String comment;
    private BusinessPeriodDTO rentalPeriod;
}
