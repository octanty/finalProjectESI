package com.example.demo.inventory.domain.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String serialNumber;

    @Enumerated(EnumType.STRING)
    EquipmentCondition equipmentCondition;

    @ManyToOne
    @NotNull
//    @JoinColumn(name = "plant_info_id")
    PlantInventoryEntry plantInfo;
}
