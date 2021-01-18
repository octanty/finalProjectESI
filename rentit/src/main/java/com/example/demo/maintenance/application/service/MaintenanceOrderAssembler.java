package com.example.demo.maintenance.application.service;

import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.common.rest.ExtendedLink;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.maintenance.application.dto.MaintenanceOrderDTO;
import com.example.demo.maintenance.domain.model.MOStatus;
import com.example.demo.maintenance.domain.model.MaintenanceOrder;
import com.example.demo.maintenance.rest.MaintenanceRestController;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Service;

//import static org.springframework.hateoas.core.DummyInvocationUtils.methodOn;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import org.springframework.http.HttpMethod;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class MaintenanceOrderAssembler extends RepresentationModelAssemblerSupport<MaintenanceOrder, MaintenanceOrderDTO> {
    public MaintenanceOrderAssembler() {
        super(MaintenanceRestController.class, MaintenanceOrderDTO.class);
    }

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @Override
    public MaintenanceOrderDTO toModel(MaintenanceOrder maintenanceOrder) {
        MaintenanceOrderDTO dto = createModelWithId(maintenanceOrder.getId(), maintenanceOrder);
        dto.set_id(maintenanceOrder.getId());
        dto.setIssueDescription(maintenanceOrder.getIssueDescription());
        dto.setMaintenancePeriod(BusinessPeriodDTO.of(maintenanceOrder.getMaintenancePeriod().getStartDate(), maintenanceOrder.getMaintenancePeriod().getEndDate()));
        dto.setNameOfSiteEngineer(maintenanceOrder.getNameOfSiteEngineer());
        dto.setNameOfConstructionSite(maintenanceOrder.getNameOfConstructionSite());
        dto.setNameOfSupplier(maintenanceOrder.getNameOfSupplier());
        dto.setStatus(maintenanceOrder.getStatus());
        dto.setPlant(plantInventoryItemAssembler.toModel(maintenanceOrder.getPlant()));

        dto.add(linkTo(methodOn(MaintenanceRestController.class).findMaintenanceOrder(dto.get_id())).withRel("fetch"));
        try {
            switch (maintenanceOrder.getStatus()) {
                case PENDING:
                    dto.add(linkTo(methodOn(MaintenanceRestController.class).acceptMaintenanceOrder(dto.get_id())).withRel("accept").withType(HttpMethod.POST.toString()));
                    dto.add(linkTo(methodOn(MaintenanceRestController.class).cancelMaintenanceOrder(dto.get_id())).withRel("cancel").withType(HttpMethod.POST.toString()));
                    dto.add(linkTo(methodOn(MaintenanceRestController.class).rejectMaintenanceOrder(dto.get_id())).withRel("reject").withType(HttpMethod.DELETE.toString()));
                    break;
                case ACCEPTED:
                    dto.add(linkTo(methodOn(MaintenanceRestController.class).cancelMaintenanceOrder(dto.get_id())).withRel("cancel").withType(HttpMethod.POST.toString()));
                    dto.add(linkTo(methodOn(MaintenanceRestController.class).completeMaintenanceOrder(dto.get_id())).withRel("complete").withType(HttpMethod.POST.toString()));
                    break;
            }
        } catch (Exception e) {
        }

        return dto;
    }
}

//public class MaintenanceOrderAssembler extends ResourceAssemblerSupport<MaintenanceOrder, MaintenanceOrderDTO> {
//
//    public MaintenanceOrderAssembler() {
//        super(MaintenanceRestController.class, MaintenanceOrderDTO.class);
//    }
//
//    @Autowired
//    PlantInventoryItemAssembler plantInventoryItemAssembler;
//
//    @Override
//    public MaintenanceOrderDTO toResource(MaintenanceOrder maintenanceOrder) {
//        MaintenanceOrderDTO dto = createResourceWithId(maintenanceOrder.getId(), maintenanceOrder);
//        dto.set_id(maintenanceOrder.getId());
//        dto.setIssueDescription(maintenanceOrder.getIssueDescription());
//        dto.setIssueDescription(maintenanceOrder.getIssueDescription());
//        dto.setMaintenancePeriod(maintenanceOrder.getMaintenancePeriod());
//        dto.setNameOfConstructionSite(maintenanceOrder.getNameOfConstructionSite());
//        dto.setNameOfSiteEngineer(maintenanceOrder.getNameOfSiteEngineer());
//        dto.setNameOfSupplier(maintenanceOrder.getNameOfSupplier());
//        dto.setStatus(maintenanceOrder.getStatus());
//        dto.setPlant(plantInventoryItemAssembler.toResource(maintenanceOrder.getPlant()));

//        dto.add(linkTo(methodOn(MaintenanceRestController.class).findMaintenanceOrder(dto.get_id())).withRel("fetch"));
//        if (maintenanceOrder.getStatus() == MOStatus.PENDING) {
//
//            try {
//                dto.add(new ExtendedLink(linkTo(methodOn(MaintenanceRestController.class).
//                    acceptMaintenanceOrder(dto.get_id())).toString(), "accept", POST));
//                dto.add(new ExtendedLink(linkTo(methodOn(MaintenanceRestController.class).
//                    cancelMaintenanceOrder(dto.get_id())).toString(), "cancel", POST));
//                dto.add(new ExtendedLink(linkTo(methodOn(MaintenanceRestController.class).
//                    rejectMaintenanceOrder(dto.get_id())).toString(), "reject", DELETE));
//            } catch (Exception ex) {
//            }
//
//        }
//
//        if (maintenanceOrder.getStatus() == MOStatus.ACCEPTED) {
//            try {
//                dto.add(new ExtendedLink(linkTo(methodOn(MaintenanceRestController.class).
//                    cancelMaintenanceOrder(dto.get_id())).toString(), "cancel", POST));
//                dto.add(new ExtendedLink(linkTo(methodOn(MaintenanceRestController.class).
//                    completeMaintenanceOrder(dto.get_id())).toString(), "completed", POST));
//                ;
//            } catch (Exception ex) {
//            }
//
//        }
//
//        return dto;
//    }
//}
