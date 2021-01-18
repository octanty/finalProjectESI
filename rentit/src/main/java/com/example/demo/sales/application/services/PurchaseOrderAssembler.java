package com.example.demo.sales.application.services;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.common.rest.ExtendedLink;
import com.example.demo.inventory.application.service.PlantInventoryEntryAssembler;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import com.example.demo.sales.domain.model.PurchaseOrder;
import com.example.demo.sales.rest.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
//import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
//import static org.sfw.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Service
public class PurchaseOrderAssembler extends RepresentationModelAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    public PurchaseOrderAssembler() {
        super(SalesRestController.class, PurchaseOrderDTO.class);
    }

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Override
    public PurchaseOrderDTO toModel(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = createModelWithId(purchaseOrder.getId(), purchaseOrder);

        dto.set_id(purchaseOrder.getId());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(), purchaseOrder.getRentalPeriod().getEndDate()));
        dto.setPlant(plantInventoryEntryAssembler.toModel(purchaseOrder.getPlant()));

        dto.add(linkTo(methodOn(SalesRestController.class).fetchPurchaseOrder(dto.get_id())).withRel("fetch"));

        try {
            switch (purchaseOrder.getStatus()) {
                case PENDING:
                    dto.add(linkTo(methodOn(SalesRestController.class).acceptPurchaseOrder(dto.get_id())).withRel("accept").withType(HttpMethod.POST.toString()));
                    dto.add(linkTo(methodOn(SalesRestController.class).rejectPurchaseOrder(dto.get_id())).withRel("reject").withType(HttpMethod.DELETE.toString()));
                    break;
                case OPEN:
                    // dto.add(linkTo(methodOn(SalesRestController.class).retrievePurchaseOrderExtensions(dto.get_id())).withRel("extend"));
            }
        } catch (Exception e) {

        }

        return dto;
    }
}

//public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
//    public PurchaseOrderAssembler() {
//        super(SalesRestController.class, PurchaseOrderDTO.class);
//    }
//
//    @Autowired
//    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
//
//    @Override
//    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
//
//        PurchaseOrderDTO dto = createResourceWithId(purchaseOrder.getId(), purchaseOrder);
//        dto.set_id(purchaseOrder.getId());
//        dto.setStatus(purchaseOrder.getStatus());
//        dto.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(), purchaseOrder.getRentalPeriod().getEndDate()));
//        dto.setPlant(plantInventoryEntryAssembler.toResource(purchaseOrder.getPlant()));
//        dto.add(linkTo(methodOn(SalesRestController.class)
//                .fetchPurchaseOrder(dto.get_id())).withRel("fetch"));
//        try {
//            switch (purchaseOrder.getStatus()) {
//                case PENDING:
//                    dto.add(new ExtendedLink(
//                            linkTo(methodOn(SalesRestController.class)
//                                    .acceptPurchaseOrder(dto.get_id())).toString(),
//                            "accept", POST));
//                    dto.add(new ExtendedLink(
//                            linkTo(methodOn(SalesRestController.class)
//                                    .rejectPurchaseOrder(dto.get_id())).toString(),
//                            "reject", DELETE));
//                    break;
//                case OPEN:
//                    dto.add(new ExtendedLink(
//                            linkTo(methodOn(SalesRestController.class)
//                                    .closePurchaseOrder(dto.get_id())).toString(),
//                            "close", DELETE));
//                    break;
//                default: break;
//            }
//        } catch (Exception e) {}
//
//
//
//        return dto;
//    }
//}
