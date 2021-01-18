package com.buildit.rental.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class ConstructionSite {
    @Id
    @GeneratedValue
    Long id;

    String address;
}
