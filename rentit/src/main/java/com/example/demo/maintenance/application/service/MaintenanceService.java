package com.example.demo.maintenance.application.service;

import com.example.demo.common.application.dto.BusinessPeriodValidator;
import com.example.demo.common.application.dto.ItemConditionValidator;
import com.example.demo.common.application.exception.*;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.application.service.PlantReservationAssembler;
import com.example.demo.inventory.domain.model.*;
import com.example.demo.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.demo.inventory.domain.repository.PlantReservationRepository;
import com.example.demo.maintenance.application.dto.MaintenanceOrderDTO;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
import com.example.demo.maintenance.domain.model.*;
import com.example.demo.maintenance.domain.repository.MaintenanceOrderRepo;
import com.example.demo.maintenance.domain.repository.MaintenancePlanRepo;
import com.example.demo.maintenance.domain.repository.MaintenanceTaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.DataBinder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaintenanceService {
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    @Autowired
    MaintenancePlanRepo maintenancePlanRepo;

    @Autowired
    MaintenanceTaskRepo maintenanceTaskRepo;

    @Autowired
    MaintenancePlanAssembler maintenancePlanAssembler;

    @Autowired
    MaintenanceTaskAssembler maintenanceTaskAssembler;

    @Autowired
    PlantReservationAssembler plantReservationAssembler;
    @Autowired
    PlantReservationRepository plantReservationRepository;

    @Autowired
    MaintenanceOrderRepo maintenacnOrderRepo;

    @Autowired
    MaintenanceOrderAssembler maintenanceOrderAssembler;

    @Autowired
    public MaintenanceService(PlantReservationRepository plantReservationRepository) {
        this.plantReservationRepository = plantReservationRepository;
    }

    public MaintenancePlanDTO createPlan(MaintenancePlanDTO planDTO) throws Exception {
        PlantInventoryItem plant = plantInventoryItemRepository.findById(planDTO.getPlant().get_id()).orElse(null);

        if (plant == null)
            throw new PlantNotFoundException("Plant NOT found");

        MaintenancePlan plan = MaintenancePlan.of(plant, planDTO.getYearOfAction());
        maintenancePlanRepo.save(plan);
        return maintenancePlanAssembler.toModel(plan);
    }


    public MaintenanceTaskDTO createTask(MaintenanceTaskDTO maintenanceTaskDTO, PlantReservation plantReservation, Long id) throws Exception {
        if (maintenancePlanRepo.findById(id).orElse(null) == null) {
            throw new PlanNotFoundException("Plan NOT found");
        }

        BusinessPeriod period = BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(), maintenanceTaskDTO.getTaskPeriod().getEndDate());

        DataBinder binder1 = new DataBinder(period);
        binder1.addValidators(new BusinessPeriodValidator());
        binder1.validate();

        if (binder1.getBindingResult().hasErrors())
            throw new InvalidTimePeriodException(binder1.getBindingResult().getAllErrors().get(0).getCode());


        DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
        binder2.addValidators(new ItemConditionValidator(plantReservationRepository));
        binder2.validate();

        if (binder2.getBindingResult().hasErrors())
            throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


        MaintenanceTask maintenanceTask = MaintenanceTask.of(plantReservation, maintenanceTaskDTO.getDescription(),
            maintenanceTaskDTO.getTypeOfWork(), maintenanceTaskDTO.getPrice(), period);

        maintenanceTaskRepo.save(maintenanceTask);
        MaintenancePlan plan = maintenancePlanRepo.getOne(id);
        plan.addTask(maintenanceTask);
        maintenancePlanRepo.save(plan);
        return maintenanceTaskAssembler.toModel(maintenanceTask);
    }

    public MaintenancePlanDTO updateMaintenancePlan(MaintenancePlanDTO planDTO, Long id) throws Exception {
        MaintenancePlan maintenancePlan = maintenancePlanRepo.findById(id).orElse(null);
        if (maintenancePlan == null)
            throw new PlanNotFoundException("Plan NOT found");

        if (planDTO.getYearOfAction() != null)
            maintenancePlan.setYearOfAction(planDTO.getYearOfAction());

        PlantInventoryItem plantInventoryItem = null;

        PlantInventoryItemDTO plantInventoryItemDTO = planDTO.getPlant();

        if (plantInventoryItemDTO != null) {
            plantInventoryItem = plantInventoryItemRepository.findById(planDTO.getPlant().get_id()).orElse(null);
            maintenancePlan.setPlant(plantInventoryItem);
        }

        maintenancePlanRepo.save(maintenancePlan);
        return maintenancePlanAssembler.toModel(maintenancePlan);
    }


    public MaintenanceTaskDTO updateTask(MaintenanceTaskDTO maintenanceTaskDTO, Long id) throws Exception {
        MaintenanceTask maintenanceTask = maintenanceTaskRepo.findById(id).orElse(null);
        if (maintenanceTask == null) {
            throw new TaskNotFoundException("Task NOT found");
        }

        PlantReservation plantReservation = maintenanceTask.getReservation();


        if (maintenanceTaskDTO.getTaskPeriod() != null) {
            BusinessPeriod period = BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(), maintenanceTaskDTO.getTaskPeriod().getEndDate());

            DataBinder binder1 = new DataBinder(period);
            binder1.addValidators(new BusinessPeriodValidator());
            binder1.validate();

            if (binder1.getBindingResult().hasErrors())
                throw new InvalidTimePeriodException(binder1.getBindingResult().getAllErrors().get(0).getCode());

            maintenanceTask.setTaskPeriod(BusinessPeriod.of(maintenanceTaskDTO.getTaskPeriod().getStartDate(),
                maintenanceTaskDTO.getTaskPeriod().getEndDate()));
        }

        if (maintenanceTaskDTO.getPrice() != null) {
            maintenanceTask.setPrice(maintenanceTaskDTO.getPrice());
        }

        if (maintenanceTaskDTO.getDescription() != null) {
            maintenanceTask.setDescription(maintenanceTaskDTO.getDescription());
        }

        if (maintenanceTaskDTO.getReservation() != null && maintenanceTaskDTO.getTypeOfWork() != null) {
            // in feature send reservation itself to binder through constructor
            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepository));
            binder2.validate();
            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


            maintenanceTask.setTypeOfWork(maintenanceTaskDTO.getTypeOfWork());
            maintenanceTask.setReservation(plantReservationRepository.findById(maintenanceTaskDTO.getReservation().get_id()).orElse(null));
        }

        if (maintenanceTaskDTO.getReservation() != null && maintenanceTaskDTO.getTypeOfWork() == null) {
            maintenanceTaskDTO.setTypeOfWork(maintenanceTask.getTypeOfWork());

            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepository));
            binder2.validate();

            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());

            maintenanceTask.setReservation(plantReservationRepository.findById(maintenanceTaskDTO.getReservation().get_id()).orElse(null));
        }

        if (maintenanceTaskDTO.getReservation() == null && maintenanceTask.getTypeOfWork() != null) {
            maintenanceTaskDTO.setReservation(plantReservationAssembler.toModel(maintenanceTask.getReservation()));

            DataBinder binder2 = new DataBinder(maintenanceTaskDTO);
            binder2.addValidators(new ItemConditionValidator(plantReservationRepository));
            binder2.validate();

            if (binder2.getBindingResult().hasErrors())
                throw new InvalidConditionException(binder2.getBindingResult().getAllErrors().get(0).getCode());


            maintenanceTask.setTypeOfWork(maintenanceTaskDTO.getTypeOfWork());
        }


        maintenanceTaskRepo.save(maintenanceTask);
        return maintenanceTaskAssembler.toModel(maintenanceTask);
    }

    public void deleteMaintenanceTask(Long id) throws Exception {
        if (maintenanceTaskRepo.findById(id).orElse(null) == null) {
            throw new TaskNotFoundException("Task NOT found");
        }


        maintenanceTaskRepo.deleteById(id);
    }

    public void deleteMaintenancePlan(Long id) throws Exception {
        if (maintenancePlanRepo.findById(id).orElse(null) == null) {
            throw new PlanNotFoundException("Plan NOT found");
        }
        maintenancePlanRepo.deleteById(id);
    }

    public MaintenancePlanDTO findPlan(Long id) throws Exception {
        if (maintenancePlanRepo.findById(id).orElse(null) == null) {
            throw new PlanNotFoundException("Plan NOT found");
        }
        MaintenancePlan plan = maintenancePlanRepo.findById(id).orElse(null);
        return maintenancePlanAssembler.toModel(plan);
    }

    public List<MaintenanceTaskDTO> getMaintenanceTasks() {
        List<MaintenanceTask> tasks = maintenanceTaskRepo.findAll();
        List<MaintenanceTaskDTO> tasksAssembler = new ArrayList<>();
        tasks.forEach(t -> {
            tasksAssembler.add(maintenanceTaskAssembler.toModel(t));
        });

        return tasksAssembler;
    }

    public MaintenanceTaskDTO findTask(Long id) throws Exception {
        if (maintenanceTaskRepo.findById(id).orElse(null) == null) {
            throw new TaskNotFoundException("Task NOT found");
        }
        MaintenanceTask task = maintenanceTaskRepo.findById(id).orElse(null);
        return maintenanceTaskAssembler.toModel(task);
    }

    public MaintenanceOrderDTO createMO(MaintenanceOrderDTO maintenanceOrderDTO) {
        MaintenanceOrder maintenanceOrder = new MaintenanceOrder();
        maintenanceOrder.setIssueDescription(maintenanceOrderDTO.getIssueDescription());
        maintenanceOrder.setMaintenancePeriod(BusinessPeriod.of(maintenanceOrderDTO.getMaintenancePeriod().getStartDate(), maintenanceOrderDTO.getMaintenancePeriod().getEndDate()));
        maintenanceOrder.setNameOfConstructionSite(maintenanceOrderDTO.getNameOfConstructionSite());
        maintenanceOrder.setNameOfSiteEngineer(maintenanceOrderDTO.getNameOfSiteEngineer());
        maintenanceOrder.setNameOfSupplier(maintenanceOrderDTO.getNameOfSupplier());
        maintenanceOrder.setStatus(MOStatus.PENDING);
        maintenanceOrder.setPlant(plantInventoryItemRepository.findById(maintenanceOrderDTO.getPlant().get_id()).get());
        maintenacnOrderRepo.save(maintenanceOrder);

        return maintenanceOrderAssembler.toModel(maintenanceOrder);
    }

    public MaintenanceOrderDTO acceptMO(Long id) throws Exception {
        MaintenanceOrder maintenanceOrder = maintenacnOrderRepo.findById(id).get();

        if (maintenanceOrder.getStatus() != MOStatus.PENDING) {
            if (maintenanceOrder.getStatus() == MOStatus.ACCEPTED) {
                throw new MaintenanceInvalidStatusChange("The maintenance order has been accepted already");
            } else {
                throw new MaintenanceInvalidStatusChange("A non-pending maintenance order cannot be accepted");
            }
        }

        maintenanceOrder.setStatus(MOStatus.ACCEPTED);
        maintenacnOrderRepo.save(maintenanceOrder);

        return maintenanceOrderAssembler.toModel(maintenanceOrder);
    }

    public MaintenanceOrderDTO rejectMO(Long id) throws Exception {
        MaintenanceOrder maintenanceOrder = maintenacnOrderRepo.findById(id).get();

        if (maintenanceOrder.getStatus() != MOStatus.PENDING) {
            if (maintenanceOrder.getStatus() == MOStatus.REJECTED) {
                throw new MaintenanceInvalidStatusChange("The maintenance order has been rejected already");
            } else {
                throw new MaintenanceInvalidStatusChange("The maintenance order has to be pending in order to reject it");
            }
        }

        maintenanceOrder.setStatus(MOStatus.REJECTED);
        maintenacnOrderRepo.save(maintenanceOrder);

        return maintenanceOrderAssembler.toModel(maintenanceOrder);
    }

    public MaintenanceOrderDTO cancelMO(Long id) throws Exception {
        MaintenanceOrder maintenanceOrder = maintenacnOrderRepo.findById(id).get();
        if (maintenanceOrder.getStatus() == MOStatus.ACCEPTED || maintenanceOrder.getStatus() == MOStatus.PENDING) {
            if (maintenanceOrder.getMaintenancePeriod().getStartDate().isAfter(LocalDate.now())) {
                maintenanceOrder.setStatus(MOStatus.CANCELED);
                maintenacnOrderRepo.save(maintenanceOrder);

                return maintenanceOrderAssembler.toModel(maintenanceOrder);
            } else {
                throw new MaintenanceAlreadyStartedException("The maintenance order has already started, it cannot be canceled");
            }
        } else {
            throw new MaintenanceAlreadyStartedException("Only pending and accepted maintenance orders can be canceled");
        }
    }


    public MaintenanceOrderDTO completeMO(Long id) throws Exception {
        MaintenanceOrder maintenanceOrder = maintenacnOrderRepo.findById(id).get();

        if (maintenanceOrder.getStatus() != MOStatus.ACCEPTED) {
            throw new MaintenanceInvalidStatusChange("Only accepted maintenance orders can be completed");
        }

        maintenanceOrder.setStatus(MOStatus.COMPLETED);
        maintenacnOrderRepo.save(maintenanceOrder);

        return maintenanceOrderAssembler.toModel(maintenanceOrder);
    }
}
