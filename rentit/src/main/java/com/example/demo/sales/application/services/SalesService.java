package com.example.demo.sales.application.services;

import com.example.demo.common.application.dto.BusinessPeriodValidator;
import com.example.demo.common.application.exception.*;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.application.service.InventoryService;
import com.example.demo.inventory.domain.model.BusinessPeriod;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.inventory.domain.model.PlantReservation;
import com.example.demo.inventory.domain.repository.InventoryRepository;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.demo.inventory.domain.repository.PlantReservationRepository;
import com.example.demo.invoicing.application.services.InvoiceService;
import com.example.demo.sales.application.dto.POExtensionDTO;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import com.example.demo.sales.domain.model.POStatus;
import com.example.demo.sales.domain.model.PurchaseOrder;
import com.example.demo.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.List;

@Service
public class SalesService {

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    PlantReservationRepository plantReservationRepository;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Autowired
    InventoryService inventoryService;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    InvoiceService invoiceService;

    public PurchaseOrderDTO createPO(PurchaseOrderDTO poDTO) throws Exception {
        BusinessPeriod period = BusinessPeriod.of(poDTO.getRentalPeriod().getStartDate(), poDTO.getRentalPeriod().getEndDate());

        DataBinder binder = new DataBinder(period);
        binder.addValidators(new BusinessPeriodValidator());
        binder.validate();

        if (binder.getBindingResult().hasErrors())
            throw new Exception("Invalid Interval");

        if (poDTO.getPlant() == null)
            throw new Exception("Invalid Plant Inventory");

        PlantInventoryEntry plant = inventoryService.findEntryById(poDTO.getPlant().get_id());

        if (plant == null)
            throw new Exception("Plant NOT Found");

        PurchaseOrder po = PurchaseOrder.of(plant, period);

        Iterator<PlantInventoryItemDTO> availableItems = inventoryService.findAvailableItems(
            plant.getName(),
            poDTO.getRentalPeriod().getStartDate(),
            poDTO.getRentalPeriod().getEndDate()).iterator();

        if (!availableItems.hasNext()) {
            po.setStatus(POStatus.REJECTED);
            throw new PlantNotAvailableException("PO modified is rejected, NO available items");
        } else {
            po.setStatus(POStatus.PENDING); //PS4
        }

        PlantInventoryItem item = inventoryService.findItemById(availableItems.next().get_id());

        PlantReservation reservation = PlantReservation.of(item, po.getRentalPeriod());
        plantReservationRepository.save(reservation);
        po.getReservations().add(reservation);

        //Payment Schedule
        po.setPaymentSchedule(poDTO.getRentalPeriod().getEndDate());

        //Total Cost
        BigDecimal totalPrice = calculatePrice(poDTO.getPlant().getPrice(), period);
        po.setTotal(totalPrice);

//        po.setTotal(BigDecimal.valueOf(777L));

        System.out.println(purchaseOrderRepository.save(po));
        ;
        return purchaseOrderAssembler.toModel(po);
    }

    public PurchaseOrderDTO modifyPO(Long id, LocalDate startDate, LocalDate endDate) throws Exception {
        BusinessPeriod period = BusinessPeriod.of(startDate, endDate);

        DataBinder binder = new DataBinder(period);
        binder.addValidators(new BusinessPeriodValidator());
        binder.validate();

        if (binder.getBindingResult().hasErrors())
            throw new Exception("Invalid Interval");

        PlantInventoryEntry plant = inventoryService.findEntryById(findPO(id).getPlant().get_id());
        if (plant == null)
            throw new Exception("Plant NOT Found");

        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);

        Iterator<PlantInventoryItemDTO> availableItems = inventoryService.findAvailablePlants(
            findPO(id).getPlant().getName(),
            startDate,
            endDate).iterator();

        if (!availableItems.hasNext()) {
            throw new PlantNotAvailableException("PO modified is rejected, NO available items");
        } else {
            po.setStatus(POStatus.PENDING);
        }

        PlantInventoryItem item = inventoryService.findItemById(availableItems.next().get_id());

        PlantReservation reservationNew = PlantReservation.of(item, period);
        plantReservationRepository.save(reservationNew);
        po.getReservations().add(reservationNew);
        po.setRentalPeriod(period);

        purchaseOrderRepository.save(po);
        return purchaseOrderAssembler.toModel(po);
    }

//    public PurchaseOrderDTO createPO(PurchaseOrderDTO poDTO) throws Exception {
//        BusinessPeriod period = BusinessPeriod.of(poDTO.getRentalPeriod().getStartDate(), poDTO.getRentalPeriod().getEndDate());
//
//        DataBinder binder = new DataBinder(period);
//        binder.addValidators(new BusinessPeriodValidator());
//        binder.validate();
//
//        if (binder.getBindingResult().hasErrors())
//            throw new Exception(binder.getBindingResult().getAllErrors().get(0).getCode());
//
//        if (poDTO.getPlant() == null)
//            throw new Exception(binder.getBindingResult().getAllErrors().get(0).getCode());
//
//        PlantInventoryEntry plant = plantInventoryEntryRepository.findById(poDTO.getPlant().get_id()).orElse(null);
//
//        if (plant == null)
//            throw new Exception("Plant NOT Found");
//
//        PurchaseOrder po = PurchaseOrder.of(plant, period);
//        purchaseOrderRepository.save(po);
//        return purchaseOrderAssembler.toResource(po);
//    }

    public PurchaseOrderDTO findPO(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        return purchaseOrderAssembler.toModel(po);
    }

    public PurchaseOrderDTO acceptPO(Long id) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        validatePO(po);
        po.setStatus(POStatus.ACCEPTED);
        purchaseOrderRepository.save(po);
        return purchaseOrderAssembler.toModel(po);
    }

    public PurchaseOrderDTO rejectPO(Long id) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        validatePO(po);
        while (!po.getReservations().isEmpty())
            plantReservationRepository.delete(po.getReservations().remove(0));
        po.setStatus(POStatus.REJECTED);
        purchaseOrderRepository.save(po);
        return purchaseOrderAssembler.toModel(po);
    }

    public PurchaseOrderDTO closePO(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElse(null);
        purchaseOrder.setStatus(POStatus.CLOSED);
        purchaseOrderRepository.save(purchaseOrder);
        return purchaseOrderAssembler.toModel(purchaseOrder);

    }

    private void validatePO(PurchaseOrder po) throws Exception {
        if (po == null)
            throw new Exception("PO Not Found");
        if (po.getStatus() != POStatus.PENDING)
            throw new Exception("PO cannot be accepted/rejected due to it is not Pending");
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPlantNotFoundException(PlantNotFoundException ex) {
    }

    @ExceptionHandler(PurchaseOrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handPurchaseOrderNotFoundException(PurchaseOrderNotFoundException ex) {
    }

    public BigDecimal calculatePrice(BigDecimal entryPrice, BusinessPeriod rentalPeriod) {
        LocalDate startDate = rentalPeriod.getStartDate();
        LocalDate endDate = rentalPeriod.getEndDate();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return entryPrice.multiply(new BigDecimal(days));
    }

    public PurchaseOrderDTO cancelPO(Long id) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        if (po.getStatus().equals(POStatus.ACCEPTED) || po.getStatus().equals(POStatus.PENDING)) {
            if (po.getRentalPeriod().getStartDate().isAfter(LocalDate.now())) {
                po.setStatus(POStatus.REJECTED);
                return purchaseOrderAssembler.toModel(purchaseOrderRepository.save(po));
            } else {
                throw new PlantAlreadyDispatchedException("Only purchase order after today can be canceled");
            }
        } else {
            throw new PlantAlreadyDispatchedException("Only pending and accepted plant hire request can be canceled");
        }
    }

    public PurchaseOrderDTO extendPO(Long id, POExtensionDTO poExtensionDTO) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElse(null);
        System.out.println(purchaseOrder.getPlant());
        System.out.println(poExtensionDTO);

        List<PlantInventoryItem> plantInvItemList = inventoryRepository.findAvailableItems(purchaseOrder.getPlant().getName(), purchaseOrder.getRentalPeriod().getEndDate().plusDays(2), poExtensionDTO.getEndDate());
        System.out.println(plantInvItemList);
        System.out.println("----------------------");
        System.out.println(purchaseOrder.getReservations().get(0).getPlant());
        System.out.println(poExtensionDTO);
        purchaseOrder.setRentalPeriod(BusinessPeriod.of(purchaseOrder.getRentalPeriod().getStartDate(), poExtensionDTO.getEndDate()));

        if (plantInvItemList.contains(purchaseOrder.getReservations().get(0).getPlant())) {
            purchaseOrderRepository.save(purchaseOrder);
        } else {
            purchaseOrder.setStatus(POStatus.REJECTED);
        }

        return purchaseOrderAssembler.toModel(purchaseOrder);
    }

    public PurchaseOrderDTO markAsDispatched(Long id) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);

        if (po.getStatus().equals(POStatus.ACCEPTED)) {
            po.setStatus(POStatus.PLANT_DISPATCHED);
            return purchaseOrderAssembler.toModel(purchaseOrderRepository.save(po));
        } else {
            throw new PlantCanNotBeDispatched("Only accepted plant hire request can be dispatched");
        }
    }

    public PurchaseOrderDTO markAsReturned(Long id) throws Exception {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        POStatus poStatus = po.getStatus();

        if (poStatus.equals(POStatus.ACCEPTED) ||
            poStatus.equals(POStatus.PLANT_DELIVERED) ||
            poStatus.equals(POStatus.PLANT_DISPATCHED) ||
            poStatus.equals(POStatus.REJECTED_BY_CUSTOMER)) {
            po.setStatus(POStatus.PLANT_RETURNED);
            invoiceService.createInvoice(id); //[PS13] - still give error
            return purchaseOrderAssembler.toModel(purchaseOrderRepository.save(po));
        } else {
            throw new PlantCanNotBeReturned("Plant can not be returned");
        }
    }

    public PurchaseOrderDTO markAsDelivered(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        po.setStatus(POStatus.PLANT_DELIVERED);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toModel(po);
    }

    public PurchaseOrderDTO markAsRejectedByCustomer(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id).orElse(null);
        po.setStatus(POStatus.REJECTED_BY_CUSTOMER);
        purchaseOrderRepository.save(po);

        return purchaseOrderAssembler.toModel(po);
    }

    public void notifyCustomer(PurchaseOrderDTO po) {
    }
}
