package com.buildit.procurement.domain.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Employee {
    @Id
    @GeneratedValue
    Long id;

    @Enumerated(EnumType.STRING)
    Role role;

    String firstName;
    String lastName;
}
