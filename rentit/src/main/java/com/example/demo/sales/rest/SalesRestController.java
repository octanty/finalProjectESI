package com.example.demo.sales.rest;

import com.example.demo.common.application.exception.*;
import com.example.demo.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.application.service.InventoryService;
import com.example.demo.sales.application.dto.POExtensionDTO;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import com.example.demo.sales.application.services.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/sales")
public class SalesRestController {
    @Autowired
    InventoryService inventoryService;

    @Autowired
    SalesService salesService;

    @GetMapping("/plants")
    public CollectionModel<PlantInventoryItemDTO> findAvailablePlants(
        @RequestParam(name = "name") String plantName,
        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return inventoryService.findAvailablePlants(plantName, startDate, endDate);
    }

//    @GetMapping("/plants")
//    public CollectionModel<PlantInventoryEntryDTO> findAvailablePlants(
//        @RequestParam(name = "name") String plantName,
//        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
//    ) {
//        return inventoryService.findAvailablePlants(plantName, startDate, endDate);
//    }

    @GetMapping("/plants/{id}")
    public PlantInventoryEntryDTO findAvailablePlant(@PathVariable Long id) {

        return inventoryService.findPlantInventoryEntry(id);
    }

//    @GetMapping("/plants-availability/{id}")
//    public EntityModel<PlantInventoryEntryAvailabilityDTO> findAvailablePlant(
//        @PathVariable Long id,
//        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
//        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
//    ) {
//        return new EntityModel<>(inventoryService.findPlantInventoryEntry(id, startDate, endDate));
////        return new EntityModel<>(inventoryService.findPlantInventoryEntry(id));
//    @GetMapping("/plants/{id}")
//    public PlantInventoryEntryDTO findAvailablePlant(@PathVariable Long id) {
//        return inventoryService.findPlantInventoryEntry(id);
//    }


    @GetMapping("/orders/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PurchaseOrderDTO fetchPurchaseOrder(@PathVariable("id") Long id) {
        return salesService.findPO(id);
    }

    @PostMapping("/orders")
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws Exception {
        PurchaseOrderDTO newlyCreatePODTO = salesService.createPO(partialPODTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(newlyCreatePODTO.getRequiredLink(IanaLinkRelations.SELF).toUri());

        return new ResponseEntity<>(newlyCreatePODTO, headers, HttpStatus.CREATED);
    }

//    @PostMapping("/orders2")
//    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO partialPODTO) throws Exception {
//        PurchaseOrderDTO newlyCreatePODTO = salesService.createPO(partialPODTO);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(new URI(newlyCreatePODTO.getId().getHref()));
//        // The above line won't working until you update PurchaseOrderDTO to extend ResourceSupport
//
//        return new ResponseEntity<PurchaseOrderDTO>(newlyCreatePODTO, headers, HttpStatus.CREATED);
//    }

//    @PatchMapping("/orders/{id}/accept")
//    public ResponseEntity<PurchaseOrderDTO> acceptPurchaseOrder(@PathVariable("id") Long id) throws Exception {
//        PurchaseOrderDTO purchaseOrderDTO = salesService.acceptPO(id);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(new URI(purchaseOrderDTO.getId().getHref()));
//
//        return new ResponseEntity<PurchaseOrderDTO>(purchaseOrderDTO, headers, HttpStatus.ACCEPTED);
//    }

    @PostMapping("/orders/{id}/accept")
    public PurchaseOrderDTO acceptPurchaseOrder(@PathVariable Long id) throws Exception {
        try {
            return salesService.acceptPO(id);
        } catch (Exception ex) {
            // Add code to Handle Exception (Change return null with the solution)
            return null;
        }
    }

    @DeleteMapping("/orders/{id}/reject") // Ex /{id}/reject
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable Long id) throws Exception {
        try {
            return salesService.rejectPO(id);
        } catch (Exception ex) {
            // Add code to Handle Exception (Change return null with the solution)
            return null;
        }
    }

//     @GetMapping("/orders/{id}/extensions")
//     public CollectionModel<EntityModel<POExtensionDTO>> retrievePurchaseOrderExtensions(@PathVariable("id") Long id) {
//         List<EntityModel<POExtensionDTO>> result = new ArrayList<>();
//         POExtensionDTO extension = new POExtensionDTO();
//         extension.setEndDate(LocalDate.now().plusWeeks(1));
//
//         result.add(new EntityModel<>(extension));
//         return new CollectionModel<>(result,
//             linkTo(methodOn(SalesRestController.class).retrievePurchaseOrderExtensions(id))
//                 .withSelfRel()
//                 .andAffordance(afford(methodOn(SalesRestController.class).requestPurchaseOrderExtension(null, id))));
//     }
//
//     @PostMapping("/orders/{id}/extensions")
//     public EntityModel<?> requestPurchaseOrderExtension(@RequestBody POExtensionDTO extension, @PathVariable("id") Long id) {
//         // Add code to handle the extension of the purchase order
// //        return new ResponseEntity<PurchaseOrderDTO>(purchaseOrderDTO,headers,HttpStatus.ACCEPTED);
//         return null;
//     }

    @PostMapping("/orders/{id}/cancel")
    public PurchaseOrderDTO cancelPO(@PathVariable Long id) throws Exception {
        return salesService.cancelPO(id);
    }

    @PatchMapping("orders/{id}/modify")
    public PurchaseOrderDTO modifyPO(
        @PathVariable Long id,
        @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) throws Exception {
        return salesService.modifyPO(id, startDate, endDate);
    }

    @PostMapping("/orders/{id}/extend")
    public PurchaseOrderDTO extendPurchaseOrder(@PathVariable Long id,
                                                @RequestBody POExtensionDTO poExtensionDTO){

        return salesService.extendPO(id, poExtensionDTO);
    }

    @PostMapping("/orders/{id}/dispatch")
    public ResponseEntity<PurchaseOrderDTO> markAsDispatched(@PathVariable Long id) throws Exception {
        PurchaseOrderDTO dispatchedPO = salesService.markAsDispatched(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(dispatchedPO.getRequiredLink(IanaLinkRelations.SELF).toUri());

        return new ResponseEntity<>(dispatchedPO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/return")
    public ResponseEntity<PurchaseOrderDTO> markAsReturned(@PathVariable Long id) throws Exception {
        PurchaseOrderDTO returnedPO = salesService.markAsReturned(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(returnedPO.getRequiredLink(IanaLinkRelations.SELF).toUri());

        return new ResponseEntity<>(returnedPO, headers, HttpStatus.OK);
    }

    @PostMapping("/orders/{id}/delivered")
    public PurchaseOrderDTO markAsDelivered(@PathVariable Long id) throws Exception {
        return salesService.markAsDelivered(id);
    }

    @PostMapping("/orders/{id}/rejected_by_customer")
    public PurchaseOrderDTO markAsRejectedByCustomer(@PathVariable Long id) throws Exception {
        return salesService.markAsRejectedByCustomer(id);
    }


    @ExceptionHandler(PlantNotAvailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MyError handlePlantNotAvailableException(PlantNotAvailableException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(PlantAlreadyDispatchedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyError handlePlantAlreadyDispatchedException(PlantAlreadyDispatchedException ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(PlantCanNotBeDispatched.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyError handlePlantCanNotBeDispatched(PlantCanNotBeDispatched ex) {
        return new MyError(ex.getMessage());
    }

    @ExceptionHandler(PlantCanNotBeReturned.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MyError handlePlantCanNotBeReturned(PlantCanNotBeReturned ex) {
        return new MyError(ex.getMessage());
    }
}
