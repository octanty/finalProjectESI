package com.example.demo.inventory.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
//import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntryDTO extends RepresentationModel<PlantInventoryEntryDTO> {
    Long _id;
    String name;
    String description;
    BigDecimal price;
}

//
//@Data
//public class PlantInventoryEntryDTO extends ResourceSupport {
//    Long _id;
//    String name;
//    String description;
//    BigDecimal price;
//}
