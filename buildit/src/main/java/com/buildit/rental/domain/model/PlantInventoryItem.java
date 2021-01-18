package com.buildit.rental.domain.model;

import com.buildit.common.rest.ExtendedLink;
import com.buildit.maintenance.application.dto.MaintenanceRequestDTO;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(force=true,access= AccessLevel.PROTECTED)
public class PlantInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String serialNumber;
    @Lob
    private ExtendedLink _xlink;

    public static PlantInventoryItem of(MaintenanceRequestDTO maintenanceRequestDTO, String GET_ENTRY_URI) {
        PlantInventoryItem pie = new PlantInventoryItem();
        pie.serialNumber = maintenanceRequestDTO.getSerialNumber();
        pie._xlink = new ExtendedLink(GET_ENTRY_URI + maintenanceRequestDTO.getPlantId(),
                "getPlantEntry", HttpMethod.GET);
        return pie;
    }
}
