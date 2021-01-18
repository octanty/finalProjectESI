package com.buildit.rental.domain.repositories;

import com.buildit.rental.domain.model.MaintenanceOrder;
import com.buildit.rental.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceOrderRepository extends JpaRepository<MaintenanceOrder, Long> {
}
