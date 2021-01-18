package com.buildit.common.application.dto;

import com.buildit.common.domain.model.BusinessPeriod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor(staticName = "of")
public class BusinessPeriodDTO {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate endDate;

    public BusinessPeriod toModel() {
        return BusinessPeriod.of(startDate, endDate);
    }
}
