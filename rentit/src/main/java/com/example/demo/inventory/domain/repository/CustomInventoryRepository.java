package com.example.demo.inventory.domain.repository;

import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.utils.Pair;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface CustomInventoryRepository {
    List<PlantInventoryEntry> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate);

    //    List<PlantInventoryItem> findAvailableItems(PlantInventoryEntry entry, LocalDate startDate, LocalDate endDate);
    List<PlantInventoryItem> findAvailableItems(String name, LocalDate startDate, LocalDate endDate);

    List<Pair<String, BigDecimal>> query1();

    List<Pair<String, Long>> query2(LocalDate startDate, LocalDate endDate);

    List<Pair<String, Long>> query3(String name, LocalDate startDate, LocalDate endDate);

    List<String> query4();

    Boolean isPlantInventoryItemExisting(Long piiId);

    Boolean isPlantInventoryEntryExisting(Long pieId);
}
