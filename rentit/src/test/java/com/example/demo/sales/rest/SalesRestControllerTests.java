package com.example.demo.maintenance.rest;

import com.example.demo.DemoApplication;
import com.example.demo.common.application.dto.BusinessPeriodDTO;
import com.example.demo.inventory.application.dto.PlantInventoryEntryDTO;
import com.example.demo.inventory.application.service.PlantInventoryEntryAssembler;
import com.example.demo.inventory.application.service.PlantInventoryItemAssembler;
import com.example.demo.inventory.application.service.PlantReservationAssembler;
import com.example.demo.inventory.domain.repository.PlantInventoryEntryRepository;
import com.example.demo.inventory.domain.repository.PlantInventoryItemRepository;
import com.example.demo.inventory.domain.repository.PlantReservationRepository;
import com.example.demo.invoicing.application.dto.InvoiceDTO;
import com.example.demo.invoicing.domain.model.Invoice;
import com.example.demo.invoicing.domain.model.InvoiceStatus;
import com.example.demo.sales.application.dto.POExtensionDTO;
import com.example.demo.sales.application.dto.PurchaseOrderDTO;
import com.example.demo.sales.domain.model.POStatus;
import com.example.demo.schedulingtasks.Notification;
import com.example.demo.schedulingtasks.NotificationRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Not;
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

import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class) // Check if the name of this class is correct or not
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SalesRestControllerTests {
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
    NotificationRepository notificationRepository;

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
//    @Sql("/plants-dataset.sql")
    public void PS4_testCreatePOAvailable() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();

        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusWeeks(1)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {
        });

        assertNotNull(createdPO);
        assertNotNull(createdPO.get_id());
        assertNotNull(createdPO.getStatus());
        assertEquals(POStatus.PENDING, createdPO.getStatus());

        // end date must be later than start date
        assertThat(createdPO.getRentalPeriod().getEndDate().isAfter(createdPO.getRentalPeriod().getStartDate()));

        // rental period must be at least today and later
        assertThat(createdPO.getRentalPeriod().getStartDate().isAfter(LocalDate.now().minusDays(1)));
    }

    @Test
    public void PS4_testCreatePONotAvailable() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();

        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusWeeks(1)));

        // PlantID 2 and 3 is not available to be rented according to our dataset
        order.setPlant(PlantInventoryEntryDTO.of(2L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message", is("PO modified is rejected, NO available items")))
            .andExpect(status().isBadRequest());

        // PlantID 2 and 3 is not available to be rented according to our dataset
        order.setPlant(PlantInventoryEntryDTO.of(3L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("message", is("PO modified is rejected, NO available items")))
            .andExpect(status().isBadRequest());


    }

    @Test
    public void PS4_testAcceptPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();

        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusWeeks(1)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        Long itemId = 1L;

        MvcResult acceptedPOAsMvcResult = mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/accept").content(mapper.writeValueAsString(itemId)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        PurchaseOrderDTO acceptedPO = mapper.readValue(acceptedPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        assertNotNull(acceptedPO);
        assertNotNull(acceptedPO.get_id());
        assertEquals(createdPO.get_id(), acceptedPO.get_id());
        assertNotNull(acceptedPO.getStatus());
        assertEquals(POStatus.ACCEPTED, acceptedPO.getStatus());

        // end date must be later than start date
        assertThat(acceptedPO.getRentalPeriod().getEndDate().isAfter(acceptedPO.getRentalPeriod().getStartDate()));

        // rental period must be at least today and later
        assertThat(acceptedPO.getRentalPeriod().getStartDate().isAfter(LocalDate.now().minusDays(1)));
    }

    @Test
    public void PS4_testRejectPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();

        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now(), LocalDate.now().plusWeeks(1)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        Long itemId = 1L;

        MvcResult rejectedPOAsMvcResult = mockMvc.perform(delete("/api/sales/orders/" + createdPO.get_id() + "/reject").content(mapper.writeValueAsString(itemId)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        PurchaseOrderDTO rejectedPO = mapper.readValue(rejectedPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        assertNotNull(rejectedPO);
        assertNotNull(rejectedPO.get_id());
        assertEquals(createdPO.get_id(), rejectedPO.get_id());
        assertNotNull(rejectedPO.getStatus());
        assertEquals(POStatus.REJECTED, rejectedPO.getStatus());

        // end date must be later than start date
        assertThat(rejectedPO.getRentalPeriod().getEndDate().isAfter(rejectedPO.getRentalPeriod().getStartDate()));

        // rental period must be at least today and later
        assertThat(rejectedPO.getRentalPeriod().getStartDate().isAfter(LocalDate.now().minusDays(1)));
    }

    @Test
    public void PS5_testViewStatusPO() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/api/sales/orders/1"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO1 = mapper.readValue(result1.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result2 = mockMvc.perform(get("/api/sales/orders/2"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO2 = mapper.readValue(result2.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result3 = mockMvc.perform(get("/api/sales/orders/3"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO3 = mapper.readValue(result3.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result4 = mockMvc.perform(get("/api/sales/orders/4"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO4 = mapper.readValue(result4.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result5 = mockMvc.perform(get("/api/sales/orders/5"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO5 = mapper.readValue(result5.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result6 = mockMvc.perform(get("/api/sales/orders/6"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO6 = mapper.readValue(result6.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});


        assertEquals(POStatus.PENDING, viewedPO1.getStatus());
        assertEquals(POStatus.ACCEPTED, viewedPO2.getStatus());
        assertEquals(POStatus.REJECTED, viewedPO3.getStatus());
        assertEquals(POStatus.PLANT_DISPATCHED, viewedPO4.getStatus());
        assertEquals(POStatus.PLANT_RETURNED, viewedPO5.getStatus());
        assertEquals(POStatus.INVOICED, viewedPO6.getStatus());
    }

    @Test
    public void PS7_testCancelPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/cancel"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
    }


    @Test
    public void PS6_testModifyPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20),
            LocalDate.of(2020, 12, 25)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result2 = mockMvc.perform(patch("/api/sales/orders/"+createdPO.get_id()+"/modify?startDate=2020-07-07&endDate=2020-08-08"))
            .andExpect(status().isOk())
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        PurchaseOrderDTO viewedPO2 = mapper.readValue(result2.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});
        assertEquals(POStatus.PENDING, viewedPO2.getStatus());

    }

    @Test
    public void PS12_extendPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        POExtensionDTO poExtensionDTO = new POExtensionDTO();
        poExtensionDTO.setEndDate(LocalDate.of(2050, 12, 25));

        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/extend").content(mapper.writeValueAsString(poExtensionDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful()).
            andExpect(jsonPath("status", is("PENDING"))).
            andReturn();

        poExtensionDTO.setEndDate(LocalDate.of(2075, 12, 25));
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/extend").content(mapper.writeValueAsString(poExtensionDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful()).
            andExpect(jsonPath("status", is("REJECTED"))).
            andReturn();
    }



    @Test
    public void PS8_dispatchPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        // Plant Dispatched is failed because the POStatus still in pending
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/dispatch"))
            .andExpect(jsonPath("message", is("Only accepted plant hire request can be dispatched")))
            .andExpect(status().isInternalServerError());

        // Accept PO
        Long itemId = 1L;
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/accept").content(mapper.writeValueAsString(itemId)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        // Plant Dispatched is successful
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/dispatch"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
    }

    @Test
    public void PS10_returnPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        // Plant can not be returned
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/return"))
            .andExpect(jsonPath("message", is("Plant can not be returned")))
            .andExpect(status().isInternalServerError());

        // Accept PO
        Long itemId = 1L;
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/accept").content(mapper.writeValueAsString(itemId)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        // Plant Dispatched is successful
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/dispatch"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();


        // Plant Returned is successful
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/return"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
    }

    @Test
    public void deliveredOrRejectedByCustomerPO() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult createdPOAsMvcResult1 = mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/delivered"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        createdPO = mapper.readValue(createdPOAsMvcResult1.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});
        assertThat(createdPO.getStatus()).isEqualTo(POStatus.PLANT_DELIVERED);
        MvcResult createdPOAsMvcResult2 = mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/rejected_by_customer"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();
        createdPO = mapper.readValue(createdPOAsMvcResult2.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        assertThat(createdPO.getStatus()).isEqualTo(POStatus.REJECTED_BY_CUSTOMER);

    }
    @Test
    public void NotificationTest() throws Exception {
        try {
            Thread.sleep(6 * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(!notificationList.isEmpty());
    }

    @Test
    @Sql("/plants-dataset.sql")
    public void NotificationNoUnpaidInvoicesTest() throws Exception {
        try {
            Thread.sleep(6 * 1000);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList.isEmpty());
    }

    @Test
    public void testSubmitInvoiceReturn() throws Exception {
        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.now().plusWeeks(1), LocalDate.now().plusWeeks(2)));
        order.setPlant(PlantInventoryEntryDTO.of(1L, "Plant 1", "Description plant 1", BigDecimal.valueOf(100L)));

        MvcResult createdPOAsMvcResult = mockMvc.perform(post("/api/sales/orders").content(mapper.writeValueAsString(order)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        PurchaseOrderDTO createdPO = mapper.readValue(createdPOAsMvcResult.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});
        mockMvc.perform(post("/api/sales/orders/" + createdPO.get_id() + "/dispatch"))
            .andExpect(status().is2xxSuccessful())
            .andReturn();

        mockMvc.perform(post("/api/invoices/"+ createdPO.get_id()+"/submitReturnPlant").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void PS15_testCreateRemittance() throws Exception {
        mockMvc.perform(post("/api/remittance/1/create").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/remittance/2/create").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/remittance/3/create").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void PS15_testAcceptRemittance() throws Exception {

       mockMvc.perform(post("/api/remittance/1/accept").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/remittance/2/accept").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/api/remittance/3/accept").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }



}
