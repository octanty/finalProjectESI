package com.buildit.procurement.application.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RemittanceAdviceDTO extends RepresentationModel<RemittanceAdviceDTO> {
    Long _id;
    String note;
}
