package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.RemittanceAdviceDTO;
import com.buildit.procurement.domain.model.RemittanceAdvice;
import com.buildit.rental.application.dto.MaintenanceOrderDTO;
import com.buildit.rental.application.dto.PurchaseOrderDTO;
import com.buildit.rental.application.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RemittanceAdviceService {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final String GET_ENTRY_RentIT = "https://rentit-team8.herokuapp.com/api/";

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    RemittanceAdviceService remittanceAdviceService;

    @Autowired
    RemittanceAdviceAssembler remittanceAdviceAssembler;

    @Autowired
    SupplierService supplierService;

    public RemittanceAdviceDTO create(Long invoiceid, String note) {
        Long supplierId = supplierService.readOne(invoiceid).get_id();
        RemittanceAdvice remittanceAdvice = null;
        RemittanceAdviceDTO remittanceAdviceDTO = remittanceAdviceAssembler.toModel(remittanceAdvice);

        // TODO: IntegrationService to send remittance advice to plant supplier

        return remittanceAdviceDTO;
    }

    public RemittanceAdviceDTO createRemittance(Long id) {
        RemittanceAdviceDTO remittanceAdviceDTO = new RemittanceAdviceDTO();
        remittanceAdviceDTO = restTemplate.postForObject(BASE_URL + "/remittance/" + id.toString() + "/create", remittanceAdviceDTO, RemittanceAdviceDTO.class);

        return remittanceAdviceDTO;
    }

    public RemittanceAdviceDTO createRemittanceRentIT(Long id, Long supplierId) {
        RemittanceAdviceDTO remittanceAdviceDTO = new RemittanceAdviceDTO();

        if(supplierId == 2){
            remittanceAdviceDTO = restTemplate.postForObject( GET_ENTRY_RentIT + "/sales/invoice/" + id.toString() + "/remittance", remittanceAdviceDTO, RemittanceAdviceDTO.class);
        }
        else{
            remittanceAdviceDTO = restTemplate.postForObject( BASE_URL + "/sales/invoice/" + id.toString() + "/remittance", remittanceAdviceDTO, RemittanceAdviceDTO.class);
        }
        return remittanceAdviceDTO;
    }
}
