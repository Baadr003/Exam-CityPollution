package com.pollu.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI pollutionAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Pollution Urbaine")
                        .description("API pour la surveillance de la qualité de l'air")
                        .version("1.0"));
    }
}