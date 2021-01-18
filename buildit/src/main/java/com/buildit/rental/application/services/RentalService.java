package com.buildit.rental.application.services;

import com.buildit.common.rest.ExtendedLink;
import com.buildit.maintenance.domain.model.MaintenanceRequest;
import com.buildit.maintenance.domain.repositories.MaintenanceRequestRepository;
import com.buildit.procurement.domain.model.PHRStatus;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.repositories.PlantHireRequestRepository;
import com.buildit.rental.application.dto.MaintenanceOrderDTO;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PlantInventoryItemDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.domain.model.MaintenanceOrder;
import com.buildit.rental.domain.model.POStatus;
import com.buildit.rental.domain.model.PurchaseOrder;
import com.buildit.rental.domain.model.Supplier;
import com.buildit.rental.domain.repositories.MaintenanceOrderRepository;
import com.buildit.rental.domain.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RentalService {
    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String RentIT_URL = "https://rentit-team8.herokuapp.com/api";
//    private static final String BASE_URL = "http://localhost:9000/api";


    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    MaintenanceOrderRepository maintenanceOrderRepository;

    @Autowired
    PlantHireRequestRepository plantHireRequestRepository;

    @Autowired
    MaintenanceRequestRepository maintenanceRequestRepository;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Autowired
    @Qualifier("halJacksonHttpMessageConverter")
    private TypeConstrainedMappingJackson2HttpMessageConverter halJacksonHttpMessageConverter;

    public RestTemplate getRestTemplateWithHalMessageConverter() {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> existingConverters = restTemplate.getMessageConverters();
        List<HttpMessageConverter<?>> newConverters = new ArrayList<>();
        newConverters.add(halJacksonHttpMessageConverter);
        newConverters.addAll(existingConverters);
        restTemplate.setMessageConverters(newConverters);
        return restTemplate;
    }

    public CollectionModel<PlantInventoryEntryDTO> findAvailablePlants(String plantName, LocalDate startDate, LocalDate endDate, Long supplierID) {
        RestTemplate testRestTemplate = getRestTemplateWithHalMessageConverter();
        String URL ="";
        if(supplierID == 2){
            URL = RentIT_URL + "/inventory/plants";
        }
        else {
            URL = BASE_URL + "/sales/plants" ;
        }

        CollectionModel<PlantInventoryEntryDTO> plants = testRestTemplate.getForObject(
            URL  + "?name={name}&startDate={start}&endDate={end}",
            CollectionModel.class, plantName, startDate, endDate);
//        return Arrays.asList(plants);
        return plants;
    }


    public PlantInventoryEntryDTO getPlant(ExtendedLink requestLink) {
        System.out.println("---->requestLink" + requestLink);
        return restTemplate.getForObject(requestLink.getHref(), PlantInventoryEntryDTO.class);
    }

    public PurchaseOrderDTO createPurchaseOrder(PlantInventoryEntryDTO plantInventoryEntryDTO, PlantHireRequest plantHireRequest, Long supplierId) {
        String URL = "";
        if(supplierId == 2){
            URL = RentIT_URL;
        }
        else {
            URL = BASE_URL;
        }
        PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
        purchaseOrderDTO.setPlant(plantInventoryEntryDTO);
        purchaseOrderDTO.setTotal(plantHireRequest.getTotalCost());
        purchaseOrderDTO.setRentalPeriod(plantHireRequest.getRentalPeriod());
        purchaseOrderDTO = restTemplate.postForObject(URL + "/sales/orders", purchaseOrderDTO, PurchaseOrderDTO.class);

        PurchaseOrder purchaseOrder = new PurchaseOrder();

//        purchaseOrder.set_xlink(new ExtendedLink(purchaseOrderDTO.getLinks().get(1).getHref(), "getPO", HttpMethod.GET));
        purchaseOrderRepository.save(purchaseOrder);
        plantHireRequest.setPo(purchaseOrder);
        plantHireRequest.setStatus(PHRStatus.ACCEPTED);
        plantHireRequestRepository.save(plantHireRequest);

        return purchaseOrderAssembler.toModel(purchaseOrder);
    }

    public MaintenanceRequest createMaintenanceOrder(
        PlantInventoryItemDTO plantInventoryItemDTO,
        MaintenanceRequest maintenanceRequest) {

        MaintenanceOrderDTO maintenanceOrderDTO = new MaintenanceOrderDTO();
        maintenanceOrderDTO.setMaintenancePeriod(maintenanceRequest.getMaintenancePeriod());
        maintenanceOrderDTO.setIssueDescription(maintenanceRequest.getIssueDescription());
        maintenanceOrderDTO.setNameOfConstructionSite(maintenanceRequest.getNameOfConstructionSite());
        maintenanceOrderDTO.setNameOfSiteEngineer(maintenanceRequest.getNameOfSiteEngineer());
        maintenanceOrderDTO.setNameOfSupplier(maintenanceRequest.getNameOfSupplier());
        maintenanceOrderDTO.setPlant(plantInventoryItemDTO);

        maintenanceOrderDTO = restTemplate.postForObject(BASE_URL + "/maintenance/order", maintenanceOrderDTO, MaintenanceOrderDTO.class);

        MaintenanceOrder maintenanceOrder = new MaintenanceOrder();
//        System.out.println(maintenanceOrderDTO.getLinks().get(0).getHref());
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXSSSSS");
//        maintenanceOrder.set_xlink(new ExtendedLink(maintenanceOrderDTO.getLinks().get(1).getHref(), "getMO", HttpMethod.GET));
        maintenanceOrderRepository.save(maintenanceOrder);

        maintenanceRequest.setMo(maintenanceOrder);
        maintenanceRequestRepository.save(maintenanceRequest);

        return maintenanceRequest;
    }

    public POStatus checkStatusPO(Long id) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(id).orElse(null);
        return purchaseOrderAssembler.toModel(purchaseOrder).getStatus();
    }

    public PurchaseOrderDTO viewPO(Long id, Long supplierId) {
        String URL = "";
        if(supplierId == 2){
            URL = RentIT_URL;
        }
        else {
            URL = BASE_URL;
        }
        return restTemplate.getForObject(URL + "/sales/orders/" + id.toString(), PurchaseOrderDTO.class);
    }

}
