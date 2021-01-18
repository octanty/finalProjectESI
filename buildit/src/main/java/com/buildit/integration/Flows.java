package com.buildit.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpMethod;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Transformers;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.scripting.dsl.Scripts;

import java.util.stream.Collectors;

@Configuration
public class Flows {
    @Bean
    IntegrationFlow scatterComponent() {
        return IntegrationFlows
                .from("req-channel")
                .publishSubscribeChannel(conf ->
                        conf.applySequence(true)
                                .subscribe(f -> f.channel("rentit-req"))
                                .subscribe(f -> f.channel("rentmt-req")))
                .get();
    }

    @Bean
    IntegrationFlow gatherComponent() {
        return IntegrationFlows.from("gather-channel")
                .aggregate(spec ->
                        spec.outputProcessor(proc ->
                                new CollectionModel<>(
                                        proc.getMessages()
                                                .stream()
                                                .map(msg -> ((CollectionModel) msg.getPayload()).getContent())
                                                .collect(Collectors.toList())))
                                .groupTimeout(2000)
                                .releaseStrategy(g -> g.size() > 1)
                                .sendPartialResultOnExpiry(true))
                .channel("rep-channel")
                .get();
    }

    @Bean
    IntegrationFlow rentItFlow() {
        return IntegrationFlows.from("rentit-req")
                .handle(Http.outboundGateway("http://localhost:8090/api/sales/plants?name={name}&startDate={startDate}&endDate={endDate}")
                        .uriVariable("name", "payload")
                        .uriVariable("startDate", "headers.startDate")
                        .uriVariable("endDate", "headers.endDate")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(String.class))
                .handle("customTransformer", "fromHALForms")
                .channel("gather-channel")
                .get();
    }


    @Bean
    IntegrationFlow rentmtFlow() {
        return IntegrationFlows
                .from("rentmt-req")
                //.handle("integrationServiceImpl", "findPlants")
                .handle(Http.outboundGateway("http://localhost:8088/api/v1/plant?filter[plant]=name==*{name}*")
                        .uriVariable("name", "payload")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(String.class))
                .transform(Scripts.processor("classpath:/JsonApi2HAL.js")
                        .lang("javascript"))
                //.transform(Transformers.fromJson(Plant[].class))
                .handle("customTransformer", "fromJson")
                .channel("gather-channel")
                .get();
    }
}
