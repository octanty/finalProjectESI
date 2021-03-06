package com.example.demo.maintenance.domain.repository;

import com.example.demo.maintenance.domain.model.MaintenanceOrder;
import com.example.demo.maintenance.domain.model.MaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceOrderRepo extends JpaRepository<MaintenanceOrder,Long> {
}
