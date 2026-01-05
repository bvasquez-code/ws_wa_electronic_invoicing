package com.ccadmin.electronicinvoicing.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("WS WA Electronic Invoicing")
                .description("API para emisión de CPE SUNAT Perú")
                .version("v1"));
    }
}
