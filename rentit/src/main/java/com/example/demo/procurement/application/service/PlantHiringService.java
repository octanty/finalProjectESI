package com.example.demo.procurement.application.service;

import com.example.demo.common.application.exception.PlantHireRequestAlreadyStartedException;
import com.example.demo.procurement.application.dto.PlantHireRequestDTO;
import com.example.demo.procurement.domain.model.PHRStatus;
import com.example.demo.procurement.domain.model.PlantHireRequest;
import com.example.demo.procurement.domain.repository.PlantHireRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class PlantHiringService {
    @Autowired
    PlantHireRequestRepository plantHireRequestRepository;

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

  /*  public PlantHireRequestDTO cancelPlantHire(Long id) throws Exception {
        PlantHireRequest plantHire = plantHireRequestRepository.findById(id).get();
        if (plantHire.getStatus() == PHRStatus.ACCEPTED || plantHire.getStatus() == PHRStatus.PENDING) {
            if (plantHire.getRentalPeriod().getStartDate().isAfter(LocalDate.now())) {
                plantHire.setStatus(PHRStatus.CANCELED);
                plantHireRequestRepository.save(plantHire);
            }
        } else {
            throw new PlantHireRequestAlreadyStartedException("Only pending and accepted plant hire request can be canceled");
        }

        return plantHireRequestAssembler.toModel(plantHire);
    }*/

}
