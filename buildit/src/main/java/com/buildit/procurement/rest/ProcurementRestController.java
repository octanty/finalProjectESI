package com.buildit.procurement.rest;

import com.buildit.common.application.exception.*;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantSupplierPHRDTO;
import com.buildit.procurement.application.dto.RemittanceAdviceDTO;
import com.buildit.procurement.application.service.PlantHireRequestAssembler;
import com.buildit.procurement.application.service.PlantHireRequestService;
import com.buildit.procurement.application.service.RemittanceAdviceService;
import com.buildit.procurement.domain.model.PHRStatus;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.application.services.PurchaseOrderAssembler;
import com.buildit.rental.application.services.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/procurements")
public class ProcurementRestController {

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

    @Autowired
    PlantHireRequestService plantHireRequestService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Autowired
    RemittanceAdviceService remittanceAdviceService;

    @GetMapping("/plants")
    public CollectionModel<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate,
            @RequestParam(name = "supplierID", required = false) Optional<Long> supplierID
        ){
        return rentalService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get(), supplierID.get());
    }


    @PostMapping("/plantHireRequest/create")
    public PlantHireRequestDTO createPlantHireRequest(
            @RequestBody PlantSupplierPHRDTO plantHireRequestDTO) throws Exception{

        return plantHireRequestService.createPlantHireRequest(plantHireRequestDTO);
    }


    @PatchMapping("/plantHireRequest/{id}/update")
    public PlantHireRequest updatePlantHireRequest(
        @PathVariable Long id,
        @RequestBody PlantSupplierPHRDTO plantSupplierPHRDTO) throws Exception {
        return plantHireRequestService.updatePlantHireRequest(plantSupplierPHRDTO, id);
    }


    @PostMapping("plantHireRequest/{id}/cancel")
    public PlantHireRequestDTO cancelPlantHireRequest(@PathVariable Long id) throws PlantHireRequestAlreadyStartedException {
        return plantHireRequestService.cancelPlantHireRequest(id);
    }

    @PostMapping("/plantHireRequest/{id}/{supplierId}/approve")
    public PurchaseOrderDTO approvePlanHireRequest(
        @PathVariable Long id, @PathVariable Long supplierId) throws Exception {

        return plantHireRequestService.approvePlantHireRequest(id, supplierId);
    }

    @DeleteMapping("/plantHireRequest/{id}/reject")
    public PlantHireRequestDTO rejectPlanHireRequest(@PathVariable Long id,
                                 @RequestBody PlantHireRequestDTO plantHireRequestDTO) throws Exception{
        return plantHireRequestService.rejectPlantHireRequest(plantHireRequestDTO, id);
    }


    @GetMapping("/plantHireRequest/checkStatus/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PHRStatus checkStatusPHR(@PathVariable Long id){
        return plantHireRequestService.checkStatusPHR(id);
    }

    @GetMapping("/plantHireRequest/viewPO/{id}/{supplierId}")
//    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrderDTO viewPO(@PathVariable Long id, @PathVariable Long supplierId){
        return plantHireRequestService.viewPO(id, supplierId);
    }

    @PatchMapping("/plantHireRequest/{id}/{supplierId}/modifyAndSubmit")
    public PurchaseOrderDTO modifyPlantRequestAndSubmit (@PathVariable Long id, @PathVariable Long supplierId,
                                  @RequestBody PlantHireRequestDTO plantHireRequestDTO) throws Exception{
        return plantHireRequestService.modifyPlantHireRequestAndSubmit(plantHireRequestDTO, id, supplierId);
    }

    @PostMapping("/remittance/{id}/create")
    public RemittanceAdviceDTO createRemittance(@PathVariable Long id){
        return remittanceAdviceService.createRemittance(id);
    }

//    @GetMapping("/submissions")
//    public List<PurchaseOrderDTO> findSubmittedPOs() {
//        return null;
//    }
//
//    @GetMapping("/submissions")
//    public List<PurchaseOrderDTO> findSubmittedPOs() {
//        return null;
//    }

    //Exception Handling
    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public MyError handlePlantNotFoundException(PlantNotFoundException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(InvalidTimePeriodException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handleInvalidTimePeriodException(InvalidTimePeriodException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(PhrStatusIsNotPending.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handlePhrStatusIsNotPending(PhrStatusIsNotPending ex) {
        return new MyError(ex.getMessage());
    }

}
