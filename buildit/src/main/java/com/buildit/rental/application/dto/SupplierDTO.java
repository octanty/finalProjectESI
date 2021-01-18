package com.buildit.rental.application.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class SupplierDTO extends RepresentationModel<SupplierDTO> {
    Long _id;
    String name;
}
