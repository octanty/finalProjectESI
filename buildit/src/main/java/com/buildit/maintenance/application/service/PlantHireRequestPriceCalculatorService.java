package com.buildit.maintenance.application.service;

import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;

import java.math.BigDecimal;

public interface PlantHireRequestPriceCalculatorService {
     BigDecimal calculatePrice(PlantInventoryEntryDTO plantInventoryEntryDTO, BusinessPeriod rentalPeriod);
}
