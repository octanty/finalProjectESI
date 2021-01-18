package com.buildit.procurement.domain.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RemittanceAdvice {
    @Id
    @GeneratedValue
    Long id;

    String note;

    @OneToOne
    Invoice invoice;
}
