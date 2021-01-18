package com.buildit.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@MessagingGateway
public interface IntegrationService {
    @Gateway(requestChannel = "req-channel", replyChannel = "rep-channel")
    Object findPlants(@Payload String name, @Header("startDate") LocalDate startDate, @Header("endDate") LocalDate endDate);
}

@Service
class IntegrationServiceImpl {
    RestTemplate restTemplate = new RestTemplate();

    public Object findPlants(Message<?> message) {
        return restTemplate.getForObject("http://localhost:8088/api/v1/plant", String.class);
    }
}
