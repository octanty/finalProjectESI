package com.buildit.rental.application.services;

import com.buildit.procurement.application.service.PlantHireRequestService;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.domain.model.PurchaseOrder;
import com.buildit.rental.domain.repositories.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseOrderService {
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    PlantHireRequestService plantHireRequestService;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    public PurchaseOrder create(String href, Long plantHireRequestId, Long externalId) {
        PurchaseOrder po = new PurchaseOrder();
        PlantHireRequest phr = plantHireRequestService.getById(plantHireRequestId);
        po.setPlantHireRequest(phr);
        po.setExternalId(externalId);

        return purchaseOrderRepository.save(po);
    }

    public CollectionModel<PurchaseOrderDTO> readAll() {
        return purchaseOrderAssembler.toCollectionModel(purchaseOrderRepository.findAll());
    }

    public PurchaseOrder readModel(Long id) {
        Optional<PurchaseOrder> po = purchaseOrderRepository.findById(id);

        if (!po.isPresent()) {
            throw new IllegalArgumentException("Cannot find PO with id " + id);
        }

        return po.get();
    }

    public PurchaseOrder findByExternalId(Long poExternalId) {
        PurchaseOrder po = purchaseOrderRepository.getFirstByExternalId(poExternalId);
        if (po == null) {
            throw new IllegalArgumentException("No Purchase Order found with external id :" + poExternalId);
        }

        return po;
    }
}
