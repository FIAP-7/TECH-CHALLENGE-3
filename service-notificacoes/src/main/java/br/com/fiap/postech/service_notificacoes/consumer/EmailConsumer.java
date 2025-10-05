package br.com.fiap.postech.service_notificacoes.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import br.com.fiap.postech.service_notificacoes.dto.NotificationDTO;


public class EmailConsumer {

    private final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = RabbitMQFanoutConfig.EMAIL_QUEUE)
    public void receiveEmailNotification(NotificationDTO message	) {
        log.info("Notificação recebida: {}", message);
    }
}
