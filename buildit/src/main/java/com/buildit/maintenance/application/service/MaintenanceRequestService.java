package com.buildit.maintenance.application.service;

import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.maintenance.application.dto.MaintenanceRequestDTO;
import com.buildit.maintenance.domain.model.MaintenanceRequest;
import com.buildit.maintenance.domain.repositories.MaintenanceRequestRepository;
import com.buildit.maintenance.domain.repositories.PlantInventoryItemRepository;
import com.buildit.rental.application.dto.MaintenanceOrderDTO;
import com.buildit.rental.application.dto.PlantInventoryItemDTO;
import com.buildit.rental.application.services.RentalService;
import com.buildit.rental.domain.model.PlantInventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MaintenanceRequestService {
    private static final String GET_ENTRY_URI = "http://localhost:8080/api/sales/plants/";

    @Autowired
    RentalService rentalService;

    private static final String BASE_URL = "http://localhost:8080/api";

//    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    MaintenanceRequestRepository maintenanceRequestRepository;

    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    public MaintenanceRequest createMaintenanceRequest(MaintenanceRequestDTO maintenanceRequestDTO) {
        BusinessPeriod businessPeriod = BusinessPeriod.of(
            maintenanceRequestDTO.getMaintenancePeriod().getStartDate(),
            maintenanceRequestDTO.getMaintenancePeriod().getEndDate());
        PlantInventoryItem item = PlantInventoryItem.of(maintenanceRequestDTO, GET_ENTRY_URI);

        MaintenanceRequest maintenanceRequest = new MaintenanceRequest();
        maintenanceRequest.setNameOfSiteEngineer(maintenanceRequestDTO.getNameOfSiteEngineer());
        maintenanceRequest.setNameOfConstructionSite(maintenanceRequestDTO.getNameOfConstructionSite());
        maintenanceRequest.setMaintenancePeriod(businessPeriod);
        maintenanceRequest.setIssueDescription(maintenanceRequestDTO.getIssueDescription());
        maintenanceRequest.setNameOfSupplier(maintenanceRequestDTO.getNameOfSupplier());
        maintenanceRequest.setPlant(item);

        PlantInventoryItemDTO plantInventoryItemDTO = new PlantInventoryItemDTO();
        plantInventoryItemDTO.set_id(maintenanceRequestDTO.getPlantId());

        plantInventoryItemRepository.save(item);
        maintenanceRequest = rentalService.createMaintenanceOrder(plantInventoryItemDTO, maintenanceRequest);

        maintenanceRequestRepository.save(maintenanceRequest);

        // We get plantEntry from rentit doesn't work until rentit implementation
//        PlantInventoryEntryDTO entryDTOFromRentit = rentalService.getPlant(entry.get_xlink());
//        plantHireRequest.setTotalCost(calculatePrice(entryDTOFromRentit, businessPeriod));
        // maintenanceRequest.setStatus(PHRStatus.PENDING);
        return maintenanceRequest;
    }

    public MaintenanceOrderDTO queryMaintenanceRequest(Long id) {
        MaintenanceOrderDTO maintenanceOrderDTO = new MaintenanceOrderDTO();
        maintenanceOrderDTO = restTemplate.getForObject(BASE_URL + "/maintenance/order/" + id.toString(), MaintenanceOrderDTO.class);
        return maintenanceOrderDTO;
    }

    public MaintenanceOrderDTO cancelMaintenanceRequest(Long id) {

        MaintenanceOrderDTO maintenanceOrderDTO = new MaintenanceOrderDTO();
        maintenanceOrderDTO = restTemplate.postForObject(BASE_URL + "/maintenance/order/" + id.toString() + "/cancel", maintenanceOrderDTO, MaintenanceOrderDTO.class);
        return maintenanceOrderDTO;
    }
}
