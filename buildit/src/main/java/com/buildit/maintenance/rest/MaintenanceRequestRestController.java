package com.buildit.maintenance.rest;

import com.buildit.maintenance.application.dto.MaintenanceRequestDTO;
import com.buildit.maintenance.application.service.MaintenanceRequestService;
import com.buildit.maintenance.domain.model.MaintenanceRequest;
import com.buildit.rental.application.dto.MaintenanceOrderDTO;
import com.buildit.rental.application.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceRequestRestController {

    @Autowired
    RentalService rentalService;

    @Autowired
    MaintenanceRequestService maintenanceRequestService;

    // @GetMapping("/request")
    // public List<PlantInventoryEntryDTO> findAvailablePlants(
    //         @RequestParam(name = "name", required = false) Optional<String> plantName,
    //         @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
    //         @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate
    //     ){
    //     return rentalService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get());
    //
    // }

    @PostMapping("/request")
    public MaintenanceRequest createMaintenanceRequest(@RequestBody MaintenanceRequestDTO maintenanceRequestDTO){

        return maintenanceRequestService.createMaintenanceRequest(maintenanceRequestDTO);
    }

    @GetMapping("/request/{id}")
    public MaintenanceOrderDTO queryMaintenanceRequest(@PathVariable Long id) {
        return maintenanceRequestService.queryMaintenanceRequest(id);
    }

    @PostMapping("/request/{id}/cancel")
    public MaintenanceOrderDTO cancelMaintenanceRequest(@PathVariable Long id){
        return maintenanceRequestService.cancelMaintenanceRequest(id);
    }


    // @PostMapping("/plants")
    // public MaintenanceRequestDTO createMaintenanceRequest(
    //         @RequestParam(name = "name", required = false) Optional<String> plantName,
    //         @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
    //         @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate
    // ){
    //     return rentalService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get());
    //
    // }
}
