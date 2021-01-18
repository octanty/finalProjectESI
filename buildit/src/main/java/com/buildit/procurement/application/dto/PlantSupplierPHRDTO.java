package com.buildit.procurement.application.dto;


import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.domain.model.PHRStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlantSupplierPHRDTO {
    Long _id;
    PHRStatus status;
    Long entryId;
    String entryName;
    String nameOfSiteEngineer;
    String nameOfConstructionSite;
    String comment;
    BusinessPeriodDTO rentalPeriod;
    Long supplierId;
}
