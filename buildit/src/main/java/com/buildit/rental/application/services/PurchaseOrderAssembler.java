package com.buildit.rental.application.services;

import com.buildit.procurement.rest.ProcurementRestController;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.domain.model.PurchaseOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;


@Service
public class PurchaseOrderAssembler extends RepresentationModelAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    private static final String GET_ENTRY_URI = "http://localhost:8080/api/sales/plants/";
    private static final String BASE_URL = "http://localhost:8080/api";

    //    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    public PurchaseOrderAssembler() {
        super(ProcurementRestController.class, PurchaseOrderDTO.class);
    }


    @Override
    public PurchaseOrderDTO toModel(PurchaseOrder purchaseOrder) {


        PurchaseOrderDTO purchaseOrderDTO = createModelWithId(purchaseOrder.getId(), purchaseOrder);
        // purchaseOrderDTO.set_id(purchaseOrder.getId());
        // purchaseOrderDTO.setStatus(purchaseOrder.get_xlink(purchaseOrderDTO.getLinks().get(0).getHref(), "getPO", HttpMethod.GET));
        // purchaseOrderDTO = restTemplate.postForObject(BASE_URL + "/sales/orders", purchaseOrderDTO, PurchaseOrderDTO.class);
        System.out.println(purchaseOrder.get_xlink().toString());
        System.out.println("##############$$$#############");
        System.out.println(BASE_URL + "/sales/orders/" + purchaseOrder.get_xlink().toString());

        PurchaseOrderDTO temp = restTemplate.getForObject(
            purchaseOrder.get_xlink().getHref(),
            PurchaseOrderDTO.class);

        // purchaseOrderDTO.setStatus(temp.getStatus());


        return temp;
    }
}
