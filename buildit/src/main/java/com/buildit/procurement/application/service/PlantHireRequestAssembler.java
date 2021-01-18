package com.buildit.procurement.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.rest.ProcurementRestController;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;


@Service
public class PlantHireRequestAssembler extends RepresentationModelAssemblerSupport<PlantHireRequest, PlantHireRequestDTO> {
    public PlantHireRequestAssembler() {
        super(ProcurementRestController.class, PlantHireRequestDTO.class);
    }

    @Override
    public PlantHireRequestDTO toModel(PlantHireRequest plantHireRequest) {

        PlantHireRequestDTO plantHireRequestDTO = createModelWithId(plantHireRequest.getId(), plantHireRequest);
        plantHireRequestDTO.set_id(plantHireRequest.getId());
        plantHireRequestDTO.setStatus(plantHireRequest.getStatus());
        plantHireRequestDTO.setEntryId(plantHireRequest.getEntry().getId());
        plantHireRequestDTO.setEntryName(plantHireRequest.getEntry().getName());
        plantHireRequestDTO.setNameOfSiteEngineer(plantHireRequest.getNameOfSiteEngineer());
        plantHireRequestDTO.setNameOfConstructionSite(plantHireRequest.getNameOfConstructionSite());
        plantHireRequestDTO.setComment(plantHireRequest.getComment());

        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(plantHireRequest.getRentalPeriod().getStartDate(), plantHireRequest.getRentalPeriod().getEndDate()));

        return plantHireRequestDTO;
    }
}
