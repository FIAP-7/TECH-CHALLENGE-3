package br.com.fiap.postech.service_agendamento.services;

import br.com.fiap.postech.service_agendamento.config.RabbitMQConfig;
import br.com.fiap.postech.service_agendamento.dto.AgendamentoEventDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public AgendamentoPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishAgendamentoEvent(AgendamentoEventDTO eventDTO) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.AGENDAMENTO_EXCHANGE, "", eventDTO);
        System.out.println("Evento de Agendamento publicado: " + eventDTO.getTipoEvento()
                + " para Agendamento ID: " + eventDTO.getAgendamentoId());
    }
}