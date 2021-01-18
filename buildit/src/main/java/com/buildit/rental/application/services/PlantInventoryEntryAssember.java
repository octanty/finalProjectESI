package com.buildit.rental.application.services;

import com.buildit.common.application.exception.PlanNotFoundException;
import com.buildit.procurement.rest.ProcurementRestController;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.domain.model.PlantInventoryEntry;
import com.buildit.rental.domain.model.PurchaseOrder;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class PlantInventoryEntryAssember extends RepresentationModelAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    private static final String GET_ENTRY_URI = "http://localhost:8080/plantInventoryEntries/";
    //    private static final String GET_ENTRY_URI = "http://localhost:8080/api/sales/plants/";
    private static final String BASE_URL = "http://localhost:8080/api";

//    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    public PlantInventoryEntryAssember() throws Exception {
        super(ProcurementRestController.class, PlantInventoryEntryDTO.class);
    }


    @Override
    public PlantInventoryEntryDTO toModel(PlantInventoryEntry plantInventoryEntry) {


        PlantInventoryEntryDTO plantInventoryEntryDTO = createModelWithId(plantInventoryEntry.getId(), plantInventoryEntry);
        // purchaseOrderDTO.set_id(purchaseOrder.getId());
        // purchaseOrderDTO.setStatus(purchaseOrder.get_xlink(purchaseOrderDTO.getLinks().get(0).getHref(), "getPO", HttpMethod.GET));
        // purchaseOrderDTO = restTemplate.postForObject(BASE_URL + "/sales/orders", purchaseOrderDTO, PurchaseOrderDTO.class);
        System.out.println(plantInventoryEntry.get_xlink().getHref());
        System.out.println("##############$$$#############1");
        System.out.println(plantInventoryEntry.get_xlink());

        System.out.println("##############$$$#############2");
        try {
            PlantInventoryEntryDTO temp = restTemplate.getForObject(
                plantInventoryEntry.get_xlink().getHref(),
                PlantInventoryEntryDTO.class);
            return temp;
        } catch (Exception ex) {
            return null;

        }


        // purchaseOrderDTO.setStatus(temp.getStatus());


    }
}
