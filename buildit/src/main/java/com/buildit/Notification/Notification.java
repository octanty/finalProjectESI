package com.buildit.Notification;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue
    Long id;

    Long po_id;

    String message;
}
