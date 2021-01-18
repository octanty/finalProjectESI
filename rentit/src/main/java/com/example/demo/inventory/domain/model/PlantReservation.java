package com.example.demo.inventory.domain.model;

import com.example.demo.maintenance.domain.model.MaintenancePlan;
import com.example.demo.sales.domain.model.PurchaseOrder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class PlantReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    BusinessPeriod schedule;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "rental_id")
    @ManyToOne
    PurchaseOrder rental;

    //    @JoinColumn(name = "plant_id")
    @ManyToOne
    @NotNull
    PlantInventoryItem plant;

    //    @JoinColumn(name = "maint_plan_id")
    @ManyToOne
    MaintenancePlan maintPlan;

    //    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof PurchaseOrder )) return false;
//        return id != null && id.equals(((PurchaseOrder) o).getId());
//    }
//
//    @Override
//    public int hashCode() {
//        return 31;
//    }
    public static PlantReservation of(PlantInventoryItem item, BusinessPeriod schedule) {
        PlantReservation reservation = new PlantReservation();
        reservation.plant = item;
        reservation.schedule = schedule;
        return reservation;
    }
}
