package com.buildit.procurement.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Data
@Entity
public class ExtensionRequest {
    @Id
    @GeneratedValue
    Long id;

    LocalDate newEndDate;
    String rejectionComment;

    @OneToOne
    PlantHireRequest plantHireRequest;
}
