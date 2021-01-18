package com.buildit.rental.domain.repositories;

import com.buildit.rental.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    PurchaseOrder getFirstByExternalId(Long externalId);
}
