package com.example.demo.maintenance.domain.repository;

import com.example.demo.maintenance.domain.model.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaintenanceTaskRepo extends JpaRepository<MaintenanceTask, Long> {
//    List<MaintenanceTask> findByPlanId();
}
