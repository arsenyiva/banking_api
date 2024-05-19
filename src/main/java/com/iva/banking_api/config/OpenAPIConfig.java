package com.iva.banking_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

/**
 * Конфигурация для настройки OpenAPI (Swagger) документации.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Настраивает и возвращает экземпляр OpenAPI с метаданными о приложении.
     *
     * @return настроенный объект OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Banking API")
                        .version("1.0")
                        .description("API for Banking application")
                        .contact(new Contact()
                                .name("Support")
                                .email("support@bankingapi.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}