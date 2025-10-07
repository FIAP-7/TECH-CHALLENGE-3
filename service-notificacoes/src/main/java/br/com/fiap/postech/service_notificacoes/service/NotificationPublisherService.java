package br.com.fiap.postech.service_notificacoes.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import br.com.fiap.postech.service_notificacoes.dto.AgendamentoDTO;

@Service
public class NotificationPublisherService {

    private final RabbitTemplate rabbitTemplate;

    NotificationPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(AgendamentoDTO agendamento) {
        String mensagem = "Agendamento: id - " + agendamento.id() + ", paciente - " + agendamento.paciente().nome() + ", data - " + agendamento.dataHora();
        rabbitTemplate.convertAndSend(RabbitMQFanoutConfig.EXCHANGE_NAME, "", mensagem);
    }
}
