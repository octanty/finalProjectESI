package com.buildit.procurement.rest.controller;

import com.buildit.BuilditApplication;

import com.buildit.procurement.application.dto.InvoiceDTO;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.service.PlantHireRequestService;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.domain.model.Invoice;
import com.buildit.procurement.domain.model.InvoiceStatus;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.application.services.PurchaseOrderService;
import com.buildit.rental.application.services.RentalService;
import com.buildit.rental.domain.model.POStatus;
import com.buildit.rental.domain.model.PurchaseOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BuilditApplication.class,
        ProcurementRestControllerTests.RentalServiceMock.class}, properties = {"spring.main.allow-bean-definition-overriding=true"})
@WebAppConfiguration
public class ProcurementRestControllerTests {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestService plantHireRequestService;

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @Configuration
    static class RentalServiceMock {
        @Bean
        public RentalService rentalService() {
            return Mockito.mock(RentalService.class);
        }
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetAllPlants() throws Exception {
        Resource responseBody = new ClassPathResource("trucks.json", this.getClass());
        List<PlantInventoryEntryDTO> list =
                mapper.readValue(responseBody.getFile(), new TypeReference<List<PlantInventoryEntryDTO>>() { });
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
//        when(rentalService.findAvailablePlants("Truck", startDate, endDate)).thenReturn(list); // TODO: PORT THIS
        MvcResult result = mockMvc.perform(
                get("/api/procurements/plants?name=Truck&startDate={start}&endDate={end}", startDate, endDate))
                .andExpect(status().isOk())
                .andReturn();

        // Add test expectations
    }

    @Test
    public void testCancelPlantHireRequest() throws Exception {
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 5");
        plantHireRequestDTO.setEntryId(1l);
        plantHireRequestDTO.setNameOfSiteEngineer("Budi");
        plantHireRequestDTO.setNameOfConstructionSite("Toni");

        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        mockMvc.perform(post("/api/procurements/plantHireRequest/1/cancel"))
            .andExpect(status().isOk());
        // Add test expectation
    }

    @Test
    public void testCheckStatusPlantHireRequest() throws Exception {
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 5");
        plantHireRequestDTO.setEntryId(1l);
        plantHireRequestDTO.setNameOfSiteEngineer("Budi");
        plantHireRequestDTO.setNameOfConstructionSite("Toni");

        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        MvcResult result1 = mockMvc.perform(get("/api/procurements/plantHireRequest/checkStatus/1"))
            .andExpect(status().isOk())
            .andReturn();

        mockMvc.perform(post("/api/procurements/plantHireRequest/1/cancel"))
            .andExpect(status().isOk());

        MvcResult result2 = mockMvc.perform(get("/api/procurements/plantHireRequest/checkStatus/1"))
            .andExpect(status().isOk())
            .andReturn();

        //PlantHireRequestDTO viewedPO1 = mapper.readValue(result1.getResponse().getContentAsString(), new TypeReference<PlantHireRequestDTO>() {});
        //PlantHireRequestDTO viewedPO2 = mapper.readValue(result2.getResponse().getContentAsString(), new TypeReference<PlantHireRequestDTO>() {});
        //assertEquals("PENDING", viewedPO1);
        //assertEquals("CANCELED", viewedPO2);
        //assertThat(viewedPO1).isEqualTo("'PENDING'");
    }



    @Test
    public void testCreatePhr() throws Exception {
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 2");
        plantHireRequestDTO.setEntryId(2l);
        plantHireRequestDTO.setNameOfSiteEngineer("sal");
        plantHireRequestDTO.setNameOfConstructionSite("nab");


        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        //checking wrong taskPeriod
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2010, 6, 20), LocalDate.of(2020, 12, 25)));
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        //checking wrong taskPeriod
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2040, 6, 20), LocalDate.of(2020, 12, 25)));
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

        //checking wrong entryId
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryId(10l);
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void testModifyPhr() throws Exception {
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setEntryId(1l);
        plantHireRequestDTO.setEntryName("entry1");
        plantHireRequestDTO.setNameOfSiteEngineer("se1");
        plantHireRequestDTO.setNameOfConstructionSite("cs1");
        plantHireRequestDTO.setComment("comment1Test");
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));

        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // successful modify
        PlantHireRequestDTO plantHireRequestDTO1 = new PlantHireRequestDTO();
        plantHireRequestDTO1.setEntryId(1l);
        plantHireRequestDTO1.setEntryName("entry1Updated");
        plantHireRequestDTO1.setNameOfSiteEngineer("se1Updated");
        plantHireRequestDTO1.setNameOfConstructionSite("cs1Updated");
        plantHireRequestDTO1.setComment("comment1Updated");
        plantHireRequestDTO1.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 11, 01), LocalDate.of(2020, 12, 31)));

        mockMvc.perform(patch("/api/procurements/plantHireRequest/1/update").content(mapper.writeValueAsString(plantHireRequestDTO1)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // wrong time
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        PlantHireRequestDTO plantHireRequestDTO2 = new PlantHireRequestDTO();
        plantHireRequestDTO2.setEntryId(1l);
        plantHireRequestDTO2.setEntryName("entry1Updated");
        plantHireRequestDTO2.setNameOfSiteEngineer("se1Updated");
        plantHireRequestDTO2.setNameOfConstructionSite("cs1Updated");
        plantHireRequestDTO2.setComment("comment1Updated");
        plantHireRequestDTO2.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 11, 1), LocalDate.of(2020, 9, 30)));

        mockMvc.perform(patch("/api/procurements/plantHireRequest/2/update").content(mapper.writeValueAsString(plantHireRequestDTO2)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void testRejectPhr() throws Exception {
        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 2");
        plantHireRequestDTO.setEntryId(2l);
        plantHireRequestDTO.setNameOfSiteEngineer("sal");
        plantHireRequestDTO.setNameOfConstructionSite("nab");

        //checking when status is pending
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(delete("/api/procurements/plantHireRequest/1/reject").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // checking when phr already rejected
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/procurements/plantHireRequest/2/approve").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(delete("/api/procurements/plantHireRequest/2/reject").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

    }

    @Test
    public void testApprovePhr() throws Exception {

        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 2");
        plantHireRequestDTO.setEntryId(1l);
        plantHireRequestDTO.setNameOfSiteEngineer("sal");
        plantHireRequestDTO.setNameOfConstructionSite("nab");

        //checking when status is pending
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/procurements/plantHireRequest/3/approve").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // checking when phr already rejected
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(post("/api/procurements/plantHireRequest/4/approve").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        mockMvc.perform(delete("/api/procurements/plantHireRequest/4/reject").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

    }


    @Test
    public void testModifyPlantRequestAndSubmit() throws Exception {

        PlantHireRequestDTO plantHireRequestDTO = new PlantHireRequestDTO();
        plantHireRequestDTO.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 12, 25)));
        plantHireRequestDTO.setEntryName("Plant 2");
        plantHireRequestDTO.setEntryId(1l);
        plantHireRequestDTO.setNameOfSiteEngineer("sal");
        plantHireRequestDTO.setNameOfConstructionSite("nab");


        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());


        PlantHireRequestDTO plantHireRequestDTO1 = new PlantHireRequestDTO();
        plantHireRequestDTO1.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2020, 6, 20), LocalDate.of(2020, 10, 25)));
        plantHireRequestDTO1.setComment("dsdsd");


        mockMvc.perform(patch("/api/procurements/plantHireRequest/1/modifyAndSubmit").content(mapper.writeValueAsString(plantHireRequestDTO1)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());


        //checking with wrong time
        mockMvc.perform(post("/api/procurements/plantHireRequest/create").content(mapper.writeValueAsString(plantHireRequestDTO)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        PlantHireRequestDTO plantHireRequestDTO2 = new PlantHireRequestDTO();
        plantHireRequestDTO2.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.of(2035, 6, 20), LocalDate.of(2020, 10, 25)));
        plantHireRequestDTO2.setComment("dsdsd");

        mockMvc.perform(patch("/api/procurements/plantHireRequest/2/modifyAndSubmit").content(mapper.writeValueAsString(plantHireRequestDTO2)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
   // @Sql("/plants-dataset.sql")
    public void testAcceptInvoice() throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderService.findByExternalId(1l);
        Invoice invoice = new Invoice();
        invoice.setPurchaseOrder(purchaseOrder);
        //invoice.setDueDate(LocalDate.now().plusWeeks(2));
        invoice.setPayableAmount(purchaseOrder.getTotal());
        invoice.setStatus(InvoiceStatus.PENDING);

        MvcResult createdInvoiceResult = mockMvc.perform(post("/api/suppliers/invoice/submit").content(mapper.writeValueAsString(invoice)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        Invoice createdInvoice = mapper.readValue(createdInvoiceResult .getResponse().getContentAsString(), new TypeReference<Invoice>() {});
        mockMvc.perform(post("/api/invoices/"+createdInvoice.getId()+"/accept").content(mapper.writeValueAsString(invoice)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    }

    @Test
    @Sql("/plants-dataset.sql")
    public void testAcceptInvoiceFailed() throws Exception {
        PurchaseOrder purchaseOrder = purchaseOrderService.findByExternalId(3l);
        Invoice invoice = new Invoice();
        invoice.setPurchaseOrder(purchaseOrder);
        //invoice.setDueDate(LocalDate.now().plusWeeks(2));
        invoice.setPayableAmount(purchaseOrder.getTotal());
        invoice.setStatus(InvoiceStatus.PENDING);

        MvcResult createdInvoiceResult = mockMvc.perform(post("/api/suppliers/invoice/submit").content(mapper.writeValueAsString(invoice)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        Invoice createdInvoice = mapper.readValue(createdInvoiceResult .getResponse().getContentAsString(), new TypeReference<Invoice>() {});
        mockMvc.perform(post("/api/invoices/"+createdInvoice.getId()+"/accept").content(mapper.writeValueAsString(invoice)).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());

    }

    @Test
    @Sql("/plants-dataset.sql")
    public void CC7_TestViewPO() throws Exception {
        MvcResult result1 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/1"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO1 = mapper.readValue(result1.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result2 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/2"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO2 = mapper.readValue(result2.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result3 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/3"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO3 = mapper.readValue(result3.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result4 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/4"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO4 = mapper.readValue(result4.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result5 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/5"))
            .andExpect(status().isOk())
            .andReturn();
        PurchaseOrderDTO viewedPO5 = mapper.readValue(result5.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() {});

        MvcResult result6 = mockMvc.perform(get("/api/procurements/plantHireRequest/viewPO/6"))
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
//    @Sql("/plants-dataset.sql")
    public void CC12_TestCreateRemittanceAdvice() throws Exception {
        mockMvc.perform(get("/api/procurements/remittance/1/create"))
            .andExpect(status().isOk());

    }
}


