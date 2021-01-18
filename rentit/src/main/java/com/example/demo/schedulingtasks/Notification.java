package com.example.demo.schedulingtasks;


import com.example.demo.sales.domain.model.PurchaseOrder;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue
    Long id;

    Long po_id;

    String message;
}
