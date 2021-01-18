package com.example.demo.invoicing.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
