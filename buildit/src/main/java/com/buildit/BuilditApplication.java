package com.buildit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class BuilditApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuilditApplication.class, args);
    }

//    @Bean
//    public OpenAPI customOpenAPI(@Value("${springdoc.version}")String appVersion) {
//        return new OpenAPI()
////            .components(new Components().addSecuritySchemes("basicScheme", new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
//            .info(new Info().title("BuildIT API").version(appVersion).description(
//                "Rest API routes for the application"
//            ));
//    }
}

