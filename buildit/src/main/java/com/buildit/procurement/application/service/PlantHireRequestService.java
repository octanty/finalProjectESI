package com.buildit.procurement.application.service;

import com.buildit.common.application.dto.BusinessPeriodValidator;
import com.buildit.common.application.exception.InvalidTimePeriodException;
import com.buildit.common.application.exception.PhrStatusIsNotPending;
import com.buildit.common.application.exception.PlantHireRequestAlreadyStartedException;
import com.buildit.common.application.exception.PlantNotFoundException;
import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantSupplierPHRDTO;
import com.buildit.procurement.domain.model.PHRStatus;
import com.buildit.procurement.domain.repositories.PlantInventoryEntryRepository;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.repositories.PlantHireRequestRepository;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.application.services.PlantInventoryEntryAssember;
import com.buildit.rental.application.services.PurchaseOrderAssembler;
import com.buildit.rental.application.services.RentalService;
import com.buildit.rental.domain.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.DataBinder;
import org.springframework.web.client.RestTemplate;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class PlantHireRequestService {
    private static final String GET_ENTRY_URI = "http://localhost:8080/api/sales/plants/";
    private static final String GET_ENTRY_RentIT = "https://rentit-team8.herokuapp.com/api/inventory/plants/";
    private static final String BASE_URL = "http://localhost:9000/api";

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestRepository plantHireRequestRepository;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    //    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    PlantInventoryEntryAssember plantInventoryEntryAssember;

    public PlantHireRequestDTO createPlantHireRequest(PlantSupplierPHRDTO plantSupplierPHRDTO) throws Exception {
        PlantHireRequest plantHireRequest = new PlantHireRequest();
        plantHireRequest.setComment(plantSupplierPHRDTO.getComment());
        plantHireRequest.setNameOfSiteEngineer(plantSupplierPHRDTO.getNameOfSiteEngineer());
        plantHireRequest.setNameOfConstructionSite(plantSupplierPHRDTO.getNameOfConstructionSite());

        BusinessPeriod businessPeriod = BusinessPeriod.of(
            plantSupplierPHRDTO.getRentalPeriod().getStartDate(),
            plantSupplierPHRDTO.getRentalPeriod().getEndDate());

        String URL = "";
        if(plantSupplierPHRDTO.getSupplierId() == 2){
            URL = GET_ENTRY_RentIT;
        }
        else {
            URL = GET_ENTRY_URI;
        }
        DataBinder binder = new DataBinder(businessPeriod);
        binder.addValidators(new BusinessPeriodValidator());
        binder.validate();

        if (binder.getBindingResult().hasErrors())
            throw new InvalidTimePeriodException(
                binder.getBindingResult().getAllErrors().get(0).getCode());

        plantHireRequest.setRentalPeriod(businessPeriod);

        PlantInventoryEntry entry = PlantInventoryEntry.of(plantSupplierPHRDTO, URL);
        // if(entry == null){
        //     throw new PlantNotFoundException("Plant Not Found");
        // }
        plantHireRequest.setEntry(entry);
        plantInventoryEntryRepository.save(entry);
        plantHireRequest.setEntry(entry);

        // PlantInventoryEntryDTO plantInventoryEntryDTO = restTemplate.getForObject(
        //     BASE_URL + entry.get_xlink().get(0).getHref(),
        //     PlantInventoryEntryDTO.class);
        if (plantInventoryEntryAssember.toModel(entry) == null) {
            throw new PlantNotFoundException("Plant not found");
        }
        plantHireRequest.setTotalCost(calculatePrice(plantInventoryEntryAssember.toModel(entry), businessPeriod));
        plantHireRequest.setStatus(PHRStatus.PENDING);

//        return plantHireRequestRepository.save(plantHireRequest);
        return plantHireRequestAssembler.toModel(plantHireRequestRepository.save(plantHireRequest));
    }


    public PlantHireRequest updatePlantHireRequest(PlantSupplierPHRDTO plantSupplierPHRDTO,
                                                        Long id) throws Exception {
        PlantHireRequest newPlantHireRequest = plantHireRequestRepository.findById(id).orElse(null);

        if (newPlantHireRequest == null) {
            throw new PlantNotFoundException("Plant Not Found");
        }
        if (plantSupplierPHRDTO.getNameOfConstructionSite() != null) {
            newPlantHireRequest.
                setNameOfConstructionSite(plantSupplierPHRDTO.getNameOfConstructionSite());
        }
        if (plantSupplierPHRDTO.getRentalPeriod() != null) {
            BusinessPeriod period = BusinessPeriod.of(
                plantSupplierPHRDTO.getRentalPeriod().getStartDate(),
                plantSupplierPHRDTO.getRentalPeriod().getEndDate());

            DataBinder binder1 = new DataBinder(period);
            binder1.addValidators(new BusinessPeriodValidator());
            binder1.validate();

            if (binder1.getBindingResult().hasErrors())
                throw new InvalidTimePeriodException(
                    binder1.getBindingResult().getAllErrors().get(0).getCode());

            newPlantHireRequest.setRentalPeriod(BusinessPeriod.of(
                plantSupplierPHRDTO.getRentalPeriod().getStartDate(),
                plantSupplierPHRDTO.getRentalPeriod().getEndDate()));

            String URL = "";
            if(plantSupplierPHRDTO.getSupplierId() == 2){
                URL = GET_ENTRY_RentIT;
            }
            else {
                URL = GET_ENTRY_URI;
            }
            // update TotalCost based on new rentalPeriod
            PlantInventoryEntry entry = PlantInventoryEntry.of(plantSupplierPHRDTO, URL);
            newPlantHireRequest.setEntry(entry);
            plantInventoryEntryRepository.save(entry);
            newPlantHireRequest.setEntry(entry);
            if (plantInventoryEntryAssember.toModel(entry) == null) {
                throw new PlantNotFoundException("Plant not found");
            }
            newPlantHireRequest.setTotalCost(calculatePrice(plantInventoryEntryAssember.toModel(entry), period));
        }

        if (newPlantHireRequest.getStatus().equals(PHRStatus.PENDING)) {
            return plantHireRequestRepository.save(newPlantHireRequest);
        } else {
            plantHireRequestRepository.save(newPlantHireRequest);
            throw new PhrStatusIsNotPending("Plant Hire Request Status is not Pending");
        }
    }


    public PurchaseOrderDTO approvePlantHireRequest(Long plantHireRequestId, Long supplierId) throws Exception {
        PlantHireRequest phr = plantHireRequestRepository.findById(plantHireRequestId).orElse(null);

        if (phr.getStatus().equals(PHRStatus.PENDING)) {
            phr.setStatus(PHRStatus.ACCEPTED);

            PlantInventoryEntryDTO entryDTO = rentalService.getPlant(phr.getEntry().get_xlink());
            return rentalService.createPurchaseOrder(entryDTO, plantHireRequestRepository.save(phr), supplierId);
        } else {
            throw new PhrStatusIsNotPending("Plant Hire Request Status is not Pending");
        }
    }

    public PlantHireRequestDTO rejectPlantHireRequest(PlantHireRequestDTO plantHireRequestDTO, Long plantHireRequestId) throws Exception {
        PlantHireRequest phr = plantHireRequestRepository.findById(plantHireRequestId).orElse(null);
        if (phr == null) {
            throw new PlantNotFoundException("Plant Not Found");
        }
        if (phr.getStatus().equals(PHRStatus.PENDING)) {
            phr.setStatus(PHRStatus.REJECTED);
            phr.setComment(plantHireRequestDTO.getComment());
            return plantHireRequestAssembler.toModel(plantHireRequestRepository.save(phr));
        } else {
            throw new PhrStatusIsNotPending("Plant Hire Request Status is not Pending");
        }

    }

    public PlantHireRequestDTO cancelPlantHireRequest(Long id) throws PlantHireRequestAlreadyStartedException {

        PlantHireRequest plantHire = plantHireRequestRepository.findById(id).get();
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        if (plantHire.getStatus() == PHRStatus.ACCEPTED || plantHire.getStatus() == PHRStatus.PENDING) {
            if (plantHire.getRentalPeriod().getStartDate().isAfter(LocalDate.now())) {
                plantHire.setStatus(PHRStatus.CANCELED);
                plantHireRequestRepository.save(plantHire);
                return plantHireRequestAssembler.toModel(plantHire);
            }
            /*else {
                plantHireRequestDTO = restTemplate.postForObject(BASE_URL + "/procurements/plantHireRequest/order/" + id.toString() + "/cancel", plantHireRequestDTO, PlantHireRequestDTO.class);
            }*/
        } else {
            throw new PlantHireRequestAlreadyStartedException("Only pending and accepted plant hire request can be canceled");
        }
        return plantHireRequestDTO;
    }

    public List<PlantHireRequest> getPlantHireRequests() {
        return plantHireRequestRepository.findAll();
    }

    public BigDecimal calculatePrice(PlantInventoryEntryDTO plantInventoryEntryDTO, BusinessPeriod rentalPeriod) {
        BigDecimal entryPrice = plantInventoryEntryDTO.getPrice();
        LocalDate startDate = rentalPeriod.getStartDate();
        LocalDate endDate = rentalPeriod.getEndDate();
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        return entryPrice.multiply(new BigDecimal(days));
    }

    public PurchaseOrderDTO modifyPlantHireRequestAndSubmit(PlantHireRequestDTO plantHireRequestDTO,
                                                            Long id, Long supplierId) throws Exception {

        PlantHireRequest newPlantHireRequest = plantHireRequestRepository.findById(id).get();
        if (newPlantHireRequest == null) {
            throw new PlantNotFoundException("Plant Not Found");
        }
        if (plantHireRequestDTO.getNameOfConstructionSite() != null) {
            newPlantHireRequest.
                setNameOfConstructionSite(plantHireRequestDTO.getNameOfConstructionSite());
        }
        if (plantHireRequestDTO.getRentalPeriod() != null) {
            BusinessPeriod period = BusinessPeriod.of(
                plantHireRequestDTO.getRentalPeriod().getStartDate(),
                plantHireRequestDTO.getRentalPeriod().getEndDate());

            DataBinder binder1 = new DataBinder(period);
            binder1.addValidators(new BusinessPeriodValidator());
            binder1.validate();

            if (binder1.getBindingResult().hasErrors())
                throw new InvalidTimePeriodException(
                    binder1.getBindingResult().getAllErrors().get(0).getCode());

            newPlantHireRequest.setRentalPeriod(BusinessPeriod.of(
                plantHireRequestDTO.getRentalPeriod().getStartDate(),
                plantHireRequestDTO.getRentalPeriod().getEndDate()));
        }



        if (newPlantHireRequest.getStatus().equals(PHRStatus.PENDING)) {

            PlantInventoryEntryDTO entryDTO = rentalService.getPlant(newPlantHireRequest.getEntry().get_xlink());
            return rentalService.createPurchaseOrder(entryDTO, plantHireRequestRepository.save(newPlantHireRequest), supplierId);
        } else {
            plantHireRequestRepository.save(newPlantHireRequest);
            throw new PhrStatusIsNotPending("Plant Hire Request Status is not Pending");
        }

    }

    public PHRStatus checkStatusPHR(Long id) {
        PlantHireRequest plantHire = plantHireRequestRepository.findById(id).orElse(null);
        return plantHireRequestAssembler.toModel(plantHire).getStatus();
    }

    public PurchaseOrderDTO viewPO(Long id, Long supplierId) {
        return rentalService.viewPO(id, supplierId);
    }

    public PlantHireRequest getById(Long id) {
        return plantHireRequestRepository.getOne(id);
    }
}
