package com.example.demo.sales.domain.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Customer {
    @Id
    @GeneratedValue
    Long id;

    String companyName;
}
