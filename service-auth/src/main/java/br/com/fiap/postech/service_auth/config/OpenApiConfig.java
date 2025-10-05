package br.com.fiap.postech.service_auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestaoRestaurantes() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Agendamento de Consultas")
                                .version("v1")
                                .description("Este microserviço contém o CRUD de agendamento de consultas e faz parte do sistema de Gestão Hospitalar - Tech Challenge 3 FIAP - Grupo 7")
                                .license(new License().name("Apache 2.0").url("https://github.com/FIAP-7/TECH-CHALLENGE-3"))
                );
    }
}
