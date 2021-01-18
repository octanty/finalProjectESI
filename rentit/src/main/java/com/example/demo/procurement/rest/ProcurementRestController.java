package com.example.demo.procurement.rest;

import com.example.demo.procurement.application.dto.PlantHireRequestDTO;
import com.example.demo.procurement.application.service.PlantHireRequestAssembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/procurements")
public class ProcurementRestController {
    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

   /* @Autowired
    PlantHiringService plantHiringService;

    @PostMapping("plantHireRequest/order/{id}/cancel")
    public PlantHireRequestDTO cancelPlantHireRequest(@PathVariable Long id) throws Exception {
        return plantHiringService.cancelPlantHire(id);
    } */
}
