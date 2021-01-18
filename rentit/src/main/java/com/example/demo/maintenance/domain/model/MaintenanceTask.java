package com.example.demo.maintenance.domain.model;

import com.example.demo.inventory.domain.model.PlantReservation;
import com.example.demo.inventory.domain.model.BusinessPeriod;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
public class MaintenanceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    @Enumerated(EnumType.STRING)
    TypeOfWork typeOfWork;

    @Column(precision = 8, scale = 2)
    BigDecimal price;

    @Embedded
    BusinessPeriod taskPeriod;

    //    @JoinColumn(name = "reservation_id")
    @ManyToOne
    @NotNull
    PlantReservation reservation;

    public static MaintenanceTask of(PlantReservation reservation, String description,
                                     TypeOfWork typeOfWork, BigDecimal price,
                                     BusinessPeriod taskPeriod) {

        MaintenanceTask task = new MaintenanceTask();
        task.reservation = reservation;
        task.description = description;
        task.typeOfWork = typeOfWork;
        task.price = price;
        task.taskPeriod = taskPeriod;

        return task;
    }
}
