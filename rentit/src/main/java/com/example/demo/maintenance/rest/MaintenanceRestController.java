package com.example.demo.maintenance.rest;


import com.example.demo.common.application.exception.*;
import com.example.demo.inventory.domain.model.PlantReservation;
import com.example.demo.inventory.domain.repository.PlantReservationRepository;
import com.example.demo.maintenance.application.dto.MaintenanceOrderDTO;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
import com.example.demo.maintenance.application.service.MaintenanceOrderAssembler;
import com.example.demo.maintenance.application.service.MaintenanceService;
import com.example.demo.maintenance.domain.model.MaintenanceOrder;
import com.example.demo.maintenance.domain.repository.MaintenanceOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceRestController {
    @Autowired
    MaintenanceService maintenanceService;

    @Autowired
    PlantReservationRepository plantReservationRepository;

    @Autowired
    MaintenanceOrderRepo maintenanceOrderRepo;

    @Autowired
    MaintenanceOrderAssembler maintenanceOrderAssembler;

    @DeleteMapping("/task/{id}")
    public void deleteMaintenanceTask(@PathVariable("id") Long id) throws Exception {
        maintenanceService.deleteMaintenanceTask(id);
    }

    @DeleteMapping("/plan/{id}")
    public void deleteMaintenancePlan(@PathVariable("id") Long id) throws Exception {
        // This will eliminate all the related tasks in cascade
        maintenanceService.deleteMaintenancePlan(id);
    }

    @GetMapping("/task")
    public ResponseEntity<List<MaintenanceTaskDTO>> getMaintenanceTasks() {
        List<MaintenanceTaskDTO> maintenanceTasks = maintenanceService.getMaintenanceTasks();
        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(new URI(maintenanceTasks.getId().getHref()));

        return new ResponseEntity<List<MaintenanceTaskDTO>>(maintenanceTasks, headers, HttpStatus.OK);
    }

    @GetMapping("/plan/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MaintenancePlanDTO fetchMaintenancePlan(@PathVariable("id") Long id) throws Exception {
        return maintenanceService.findPlan(id);
    }

    @GetMapping("/plan/task/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MaintenanceTaskDTO fetchMaintenanceTask(@PathVariable("id") Long id) throws Exception {
        return maintenanceService.findTask(id);
    }

    @PostMapping("/plan")
    public ResponseEntity<MaintenancePlanDTO> createPlan(@RequestBody MaintenancePlanDTO partialPlanDTO) throws Exception {
        MaintenancePlanDTO newlyCreatedPlanDTO = maintenanceService.createPlan(partialPlanDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyCreatedPlanDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        headers.setLocation(new URI(newlyCreatedPlanDTO.get_id().getHref()));

        return new ResponseEntity<MaintenancePlanDTO>(newlyCreatedPlanDTO, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/plan/{id}")
    public ResponseEntity<MaintenancePlanDTO> updateMaintenancePlan(@RequestBody MaintenancePlanDTO partialPlanDTO, @PathVariable("id") Long id) throws Exception {
        // System.out.print(partialPlanDTO);
        MaintenancePlanDTO newlyCreatedPlanDTO = maintenanceService.updateMaintenancePlan(partialPlanDTO, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyCreatedPlanDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        headers.setLocation(new URI(newlyCreatedPlanDTO.getId().getHref()));
        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport

        return new ResponseEntity<MaintenancePlanDTO>(newlyCreatedPlanDTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/plan/{id}/task")
    public ResponseEntity<MaintenanceTaskDTO> createTask(@RequestBody MaintenanceTaskDTO partialTaskDTO, @PathVariable("id") Long id) throws Exception {
        PlantReservation plantReservation = plantReservationRepository.findById(partialTaskDTO.getReservation().get_id()).orElse(null);

        MaintenanceTaskDTO newlyCreatedTaskDTO = maintenanceService.createTask(partialTaskDTO, plantReservation, id);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyCreatedTaskDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        headers.setLocation(new URI(newlyCreatedTaskDTO.getId().getHref()));

        return new ResponseEntity<MaintenanceTaskDTO>(newlyCreatedTaskDTO, headers, HttpStatus.CREATED);
    }

    @PatchMapping("/plan/task/{id}")
    public ResponseEntity<MaintenanceTaskDTO> updateTask(@RequestBody MaintenanceTaskDTO partialTaskDTO, @PathVariable("id") Long id) throws Exception {

        MaintenanceTaskDTO newlyUpdatedTaskDTO = maintenanceService.updateTask(partialTaskDTO, id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyUpdatedTaskDTO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        headers.setLocation(new URI(newlyUpdatedTaskDTO.getId().getHref()));

        return new ResponseEntity<MaintenanceTaskDTO>(newlyUpdatedTaskDTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/order")
    public ResponseEntity<MaintenanceOrderDTO> createMaintenanceOrder(@RequestBody MaintenanceOrderDTO maintenanceOrderDTO) throws Exception {

        MaintenanceOrderDTO newlyCreatedMO = maintenanceService.createMO(maintenanceOrderDTO);


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyCreatedMO.getRequiredLink(IanaLinkRelations.SELF).toUri());
//        headers.setLocation(new URI(newlyCreatedMO.getId().getHref()));

        return new ResponseEntity<MaintenanceOrderDTO>(newlyCreatedMO, headers, HttpStatus.CREATED);
    }

    @GetMapping("/order/{id}")
    public MaintenanceOrderDTO findMaintenanceOrder(@PathVariable Long id) {
        MaintenanceOrder maintenanceOrder = maintenanceOrderRepo.findById(id).get();

        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(new URI(newlyCreatedPlanDTO.getId().getHref()));
        // // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport
        //
        // return new ResponseEntity<MaintenancePlanDTO>(newlyCreatedPlanDTO, headers, HttpStatus.CREATED);
        return maintenanceOrderAssembler.toModel(maintenanceOrder);
    }

    @PostMapping("/order/{id}/accept")
    public MaintenanceOrderDTO acceptMaintenanceOrder(@PathVariable Long id) throws Exception {
        return maintenanceService.acceptMO(id);
    }

    @DeleteMapping("/order/{id}/reject")
    public MaintenanceOrderDTO rejectMaintenanceOrder(@PathVariable Long id) throws Exception {
        return maintenanceService.rejectMO(id);
    }

    @PostMapping("/order/{id}/cancel")
    public MaintenanceOrderDTO cancelMaintenanceOrder(@PathVariable Long id) throws Exception {
        return maintenanceService.cancelMO(id);
    }

    @PostMapping("/order/{id}/complete")
    public MaintenanceOrderDTO completeMaintenanceOrder(@PathVariable Long id) throws Exception {
        return maintenanceService.completeMO(id);
    }

    //ExceptionHandlers
    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyError handPlantNotFoundException(PlantNotFoundException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(PlanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyError handPlantNotFoundException(PlanNotFoundException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyError handPlantNotFoundException(TaskNotFoundException ex) {
        return new MyError(ex.getMessage());

    }

    @ExceptionHandler(InvalidTimePeriodException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleInvalidTimePeriodException(InvalidTimePeriodException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(InvalidConditionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MyError handleInvalidCondition(InvalidConditionException ex) {

        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(MaintenanceAlreadyStartedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MyError handleMaintenanceAlreadyStarted(MaintenanceAlreadyStartedException ex) {
        return new MyError(ex.getMessage());

    }
}
