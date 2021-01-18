package com.buildit.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomTransformer {
//    @Autowired
//    @Qualifier("_halObjectMapper")
//    ObjectMapper mapper;
//
//    public CollectionModel<EntityModel<Plant>> fromJson(String json) {
//        try {
//            List<Plant> plants = mapper.readValue(json, new TypeReference<List<Plant>>() {
//            });
//            return new CollectionModel<>(plants.stream()
//                .map(p -> new EntityModel<>(p, new Link("http://localhost:8088/api/v1/plant/" + p._id)))
//                .collect(Collectors.toList()));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public CollectionModel<EntityModel<Plant>> fromHALForms(String json) {
//        try {
//            return mapper.readValue(json, new TypeReference<CollectionModel<EntityModel<Plant>>>() {
//            });
//        } catch (Exception e) {
//            return null;
//        }
//    }
}
