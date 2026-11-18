package com.masudiqbal.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customerServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enterprise Banking - Customer Service API")
                        .description("""
                                REST API for managing banking customers.

                                This service supports customer creation,
                                profile updates, customer retrieval,
                                pagination and customer deactivation.
                                """)
                        .version("v1")
                        .contact(new Contact()
                                .name("Masud Ikbal")
                                .url("https://github.com/masud-iqbal")));
    }
}
