package br.com.fiap.postech.service_agendamento.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String AGENDAMENTO_EXCHANGE = "agendamento.exchange";

    @Bean
    public Exchange agendamentoExchange() {
        return new FanoutExchange(AGENDAMENTO_EXCHANGE);
    }
}