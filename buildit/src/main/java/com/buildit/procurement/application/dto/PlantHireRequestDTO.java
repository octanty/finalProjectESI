package com.buildit.procurement.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.domain.model.PHRStatus;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

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
