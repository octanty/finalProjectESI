package com.buildit.rental.domain.model;

import com.buildit.common.rest.ExtendedLink;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.domain.model.PlantHireRequest;
import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    Long externalId;

    @OneToOne(cascade = CascadeType.ALL)
    PlantHireRequest plantHireRequest;

    @Column(precision=8,scale=2)
    BigDecimal total;

    @OneToOne
    Invoice invoice;

    @Lob
    private ExtendedLink _xlink;

    // public static PlantInventoryEntry of(PlantHireRequestDTO plantHireRequestDTO, String GET_ENTRY_URI) {
    //     PlantInventoryEntry pie = new PlantInventoryEntry();
    //     pie.name = plantHireRequestDTO.getEntryName();
    //     pie._xlink = new ExtendedLink(GET_ENTRY_URI + plantHireRequestDTO.getEntryId(),
    //             "getPlantEntry", HttpMethod.GET);;
    //     return pie;
    // }
}
