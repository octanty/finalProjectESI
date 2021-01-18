package com.example.demo.maintenance.domain.model;

import com.example.demo.inventory.domain.model.PlantInventoryItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class MaintenancePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Integer yearOfAction;

//    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
//    @JoinColumn(name = "maintenance_plan_id")
//    List<MaintenanceTask> tasks;

    //    @JoinColumn(name = "plant_id")
    @ManyToOne
    @NotNull
    PlantInventoryItem plant;

    //    @JoinColumn(name = "plan_id")
    @OneToMany(cascade = {CascadeType.ALL})
    List<MaintenanceTask> tasks;

    public static MaintenancePlan of(PlantInventoryItem item, Integer yearOfAction) {
        MaintenancePlan plan = new MaintenancePlan();
        plan.plant = item;
        plan.yearOfAction = yearOfAction;
        plan.tasks = new ArrayList<>();

        return plan;
    }

    public void addTask(MaintenanceTask maintenanceTask) {
        this.tasks.add(maintenanceTask);
    }
}
