package com.example.demo.inventory.domain.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class PlantInventoryEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;
    String description;

    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
