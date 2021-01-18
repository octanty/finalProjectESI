package com.example.demo.maintenance.rest;

import com.example.demo.DemoApplication;
import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.dto.PlantInventoryItemDTO;
import com.example.demo.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.demo.inventory.application.service.PlantInventoryEntryAssembler;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.inventory.application.service.PlantReservationAssembler;
import com.example.demo.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.demo.inventory.domain.repository.PlantReservationRepository;
import com.example.demo.maintenance.application.dto.MaintenancePlanDTO;
import com.example.demo.maintenance.application.dto.MaintenanceTaskDTO;
import com.example.demo.maintenance.domain.model.TypeOfWork;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class) // Check if the name of this class is correct or not
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MaintenanceRestControllerTest {
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    @Autowired
    PlantInventoryItemAssembler plantInventoryItemAssembler;

    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;

    @Autowired
    PlantInventoryEntryRepository repo;

    @Autowired
    PlantReservationAssembler plantReservationAssembler;

    @Autowired
    PlantReservationRepository reservationRepo;

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testCreateTask() throws Exception {

        MaintenanceTaskDTO task = new MaintenanceTaskDTO();
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setDescription("dsd");
        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));


        //cheking taskPeriod
        mockMvc.perform(post("/api/maintenance/plan/1/task").content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate should be before endDate"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2012, 6, 20), LocalDate.of(2020, 12, 25)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate must be in the future"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(null, LocalDate.of(2020, 12, 25)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate cannot be null"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));


        //Checking combinations of workType and equipmentCondition
        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(post("/api/maintenance/plan/1/task").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());


    }


    @Test
    @Sql("/plants-dataset.sql")
    public void testPutTask() throws Exception {

        MaintenanceTaskDTO task = new MaintenanceTaskDTO();

        //cheking taskPeriod
        mockMvc.perform(patch("/api/maintenance/plan/task/1").content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now()));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate should be before endDate"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2012, 6, 20), LocalDate.of(2020, 12, 25)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate must be in the future"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(null, LocalDate.of(2020, 12, 25)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(jsonPath("message", is("startDate cannot be null"))).
                andExpect(status().isBadRequest());

        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));

        //checking all fields
        task.setPrice(BigDecimal.valueOf(3l));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setDescription("sas");
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());


        //Checking combinations of workType and equipmentCondition
        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.CORRECTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(1l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(2l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(3l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());

        task.setTypeOfWork(TypeOfWork.PREVENTIVE);
        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(4l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/task/1").
                content(mapper.writeValueAsString(task)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isBadRequest());


    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testFindAvailablePlants() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/sales/plants?name=Plant&startDate=2019-01-01&endDate=2019-02-01"))
                .andExpect(status().isOk())
                .andExpect(header().string("Location", isEmptyOrNullString()))
                .andReturn();

        List<PlantInventoryEntryDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryEntryDTO>>() {
        });

        assertThat(plants.size()).isEqualTo(4);
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testCreateTaskDefaultScenario() throws Exception {
        MaintenanceTaskDTO task = new MaintenanceTaskDTO();

        PlantInventoryItemDTO itemToBeReserved = findAnyItemForPlant();

        task.setReservation(plantReservationAssembler.toModel(reservationRepo.findById(itemToBeReserved.get_id()).orElse((null))));
        task.setTypeOfWork(TypeOfWork.OPERATIVE);
        task.setTaskPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20),
                LocalDate.of(2020, 12, 25)));


        //cheking taskPeriod
        MvcResult createdTaskMvcResult = mockMvc.perform(patch("/api/maintenance/plan/task/1")
                .content(mapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        MaintenanceTaskDTO createdTask = mapper.readValue(createdTaskMvcResult.getResponse().getContentAsString(),
                new TypeReference<MaintenanceTaskDTO>() {
                });

        assertNotNull(createdTask);
        assertNotNull(createdTask.get_id());
        assertNotNull(createdTask.getTaskPeriod());
        assertEquals(TypeOfWork.OPERATIVE, createdTask.getTypeOfWork());

        // check reference to a plant inventory item
        assertNotNull(createdTask);
        assertNotNull(createdTask.getReservation().getPlant());
        assertNotNull(createdTask.getReservation().getPlant().get_id());
        assertNotNull(createdTask.getReservation().getPlant().getSerialNumber());
        assertEquals(itemToBeReserved.get_id(), createdTask.getReservation().getPlant().get_id());
        assertEquals(itemToBeReserved.getSerialNumber(), createdTask.getReservation().getPlant().getSerialNumber());

        // check maintenance task period
        assertNotNull(createdTask.getTaskPeriod());
        assertNotNull(createdTask.getTaskPeriod().getStartDate());
        assertNotNull(createdTask.getTaskPeriod().getEndDate());

        // check that end date must be after start date
        assertThat(createdTask.getTaskPeriod().getEndDate().isAfter(createdTask.getTaskPeriod().getStartDate()));

        // check the task period must be in future, starting from today's date
        assertThat(createdTask.getTaskPeriod().getStartDate().isAfter(LocalDate.now().minusDays(1)));
    }

    private PlantInventoryItemDTO findAnyItemForPlant() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/pitems/items?pieId=1&startDate=2019-06-20&endDate=2019-12-25")).andReturn();

        List<PlantInventoryItemDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryItemDTO>>() {
        });

        PlantInventoryItemDTO plant = plants.get(1);

        return plant;
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testGetMaintenancePlan() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/maintenance/plan/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Maintenance Plan", isEmptyOrNullString()))
                .andReturn();
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testGetMaintenanceTask() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/maintenance/plan/task/1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Maintenance Task", isEmptyOrNullString()))
                .andReturn();
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testPutMaintenancePlan() throws Exception {

        MaintenancePlanDTO maintenancePlan = new MaintenancePlanDTO();

        //cheking taskPeriod
        mockMvc.perform(patch("/api/maintenance/plan/1").content(mapper.writeValueAsString(maintenancePlan)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        maintenancePlan.setYearOfAction(2015);
        mockMvc.perform(patch("/api/maintenance/plan/1").
                content(mapper.writeValueAsString(maintenancePlan)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        maintenancePlan.setPlant(plantInventoryItemAssembler.toModel(plantInventoryItemRepository.findById(2l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/1").
                content(mapper.writeValueAsString(maintenancePlan)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());

        maintenancePlan.setYearOfAction(2016);
        maintenancePlan.setPlant(plantInventoryItemAssembler.toModel(plantInventoryItemRepository.findById(3l).orElse(null)));
        mockMvc.perform(patch("/api/maintenance/plan/1").
                content(mapper.writeValueAsString(maintenancePlan)).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isCreated());
    }
}
