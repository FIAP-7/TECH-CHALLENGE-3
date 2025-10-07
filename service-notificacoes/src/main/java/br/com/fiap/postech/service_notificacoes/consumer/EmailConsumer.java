package br.com.fiap.postech.service_notificacoes.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import br.com.fiap.postech.service_notificacoes.dto.AgendamentoDTO;
import br.com.fiap.postech.service_notificacoes.service.NotificationPublisherService;

@Component
public class EmailConsumer {

    private final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private NotificationPublisherService service;
    public EmailConsumer(NotificationPublisherService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitMQFanoutConfig.EMAIL_QUEUE)
    public void receiveEmailNotification(AgendamentoDTO agendamento) {
        log.info("Notificação recebida: {}", agendamento);
        service.sendNotification(agendamento);
    }
}
