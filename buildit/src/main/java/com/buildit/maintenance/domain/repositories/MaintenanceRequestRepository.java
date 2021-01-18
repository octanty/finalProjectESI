package com.buildit.maintenance.domain.repositories;

import com.buildit.maintenance.domain.model.MaintenanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRequestRepository extends JpaRepository<MaintenanceRequest, Long> {
}
