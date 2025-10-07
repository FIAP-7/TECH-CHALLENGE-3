package br.com.fiap.postech.service_notificacoes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ServiceNotificacoesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceNotificacoesApplication.class, args);
    }
}