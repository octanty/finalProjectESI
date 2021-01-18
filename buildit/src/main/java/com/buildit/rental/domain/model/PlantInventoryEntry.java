package com.buildit.rental.domain.model;

import com.buildit.common.rest.ExtendedLink;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantSupplierPHRDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PlantInventoryEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Lob
    private ExtendedLink _xlink;

 /*   public static PlantInventoryEntry of(PlantHireRequestDTO plantHireRequestDTO, String GET_ENTRY_URI) {
        PlantInventoryEntry pie = new PlantInventoryEntry();
        pie.name = plantHireRequestDTO.getEntryName();
        pie._xlink = new ExtendedLink(GET_ENTRY_URI + plantHireRequestDTO.getEntryId(),
            "getPlantEntry", HttpMethod.GET);
        return pie;
    } */

    public static PlantInventoryEntry of(PlantSupplierPHRDTO plantSupplierPHRDTO, String GET_ENTRY_URI) {
        PlantInventoryEntry pie = new PlantInventoryEntry();
        pie.name = plantSupplierPHRDTO.getEntryName();
        pie._xlink = new ExtendedLink(GET_ENTRY_URI + plantSupplierPHRDTO.getEntryId(),
            "getPlantEntry", HttpMethod.GET);
        return pie;
    }

}
