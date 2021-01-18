package com.example.demo.inventory.application.service;

import com.example.demo.inventory.application.dto.PlantInventoryEntryAvailabilityDTO;
import com.example.demo.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.domain.model.PlantInventoryItem;
import com.example.demo.inventory.domain.model.PlantInventoryEntry;
import com.example.demo.inventory.domain.repository.InventoryRepository;
import com.example.demo.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    PlantInventoryEntryAvailabilityAssembler plantInventoryEntryAvailabilityAssembler;

    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    public PlantInventoryEntry findEntryById(Long id) {
        return plantInventoryEntryRepository.findById(id).orElse(null);
    }

    public PlantInventoryItem findItemById(Long id) {
        return plantInventoryItemRepository.findById(id).orElse(null);
    }

    public CollectionModel<PlantInventoryItemDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
        List<PlantInventoryItem> entries = inventoryRepository.findAvailableItems(name, startDate, endDate);
//        List<PlantInventoryEntry> entries = inventoryRepository.findAll();

        return plantInventoryItemAssembler.toCollectionModel(entries);
    }

//    public CollectionModel<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
//        List<PlantInventoryEntry> entries = inventoryRepository.findAvailablePlants(name, startDate, endDate);
////        List<PlantInventoryEntry> entries = inventoryRepository.findAll();
//
//        return plantInventoryEntryAssembler.toCollectionModel(entries);
//    }

//    public PlantInventoryItem findPlantInventoryItem(Long id) {
//        return plantInventoryItemRepository.getOne(id);
//    }


    public PlantInventoryEntryDTO findPlantInventoryEntry(Long id) {
        PlantInventoryEntry plant = plantInventoryEntryRepository.getOne(id);

//        return plant;
        return plantInventoryEntryAssembler.toModel(plantInventoryEntryRepository.getOne(id));
    }

    public PlantInventoryEntryAvailabilityDTO findPlantInventoryEntry2(Long id, LocalDate startDate, LocalDate endDate) {
        PlantInventoryEntry plant = plantInventoryEntryRepository.getOne(id);

        PlantInventoryEntryAvailabilityDTO plantInventoryEntryAvailabilityDTO = plantInventoryEntryAvailabilityAssembler.toModel(plant);
        plantInventoryEntryAvailabilityDTO.setAvailable(false);
        plantInventoryEntryAvailabilityDTO.setTotalPrice(BigDecimal.TEN);

        return plantInventoryEntryAvailabilityDTO;
//                return plantInventoryEntryAssembler.toModel(plantInventoryEntryRepository.getOne(id));
    }


    public Boolean isPlantInventoryItemExisting(Long id) {
        return inventoryRepository.isPlantInventoryItemExisting(id);
    }

    public Boolean isPlantInventoryEntryExisting(Long id) {
        return inventoryRepository.isPlantInventoryEntryExisting(id);
    }

    public CollectionModel<PlantInventoryItemDTO> findAvailableItems(String name, LocalDate startDate, LocalDate endDate) {
        List<PlantInventoryItem> items = inventoryRepository.findAvailableItems(name, startDate, endDate);
        return plantInventoryItemAssembler.toCollectionModel(items);
    }

    public List<PlantInventoryItem> findAvailableItemsE(String name, LocalDate startDate, LocalDate endDate) {
        return inventoryRepository.findAvailableItems(name, startDate, endDate);
    }

//    public List<PlantInventoryEntryDTO> findAvailablePlants(String name, LocalDate startDate, LocalDate endDate) {
//        // Complete the implementation here -- assembler required
//        // Remove the return of the empty list
////        return new ArrayList<PlantInventoryEntryDTO>();
//        List<PlantInventoryEntry> res = inventoryRepository.findAvailablePlants(name, startDate, endDate);
//        return plantInventoryEntryAssembler.toResources(res);
//    }
}
