package br.com.fiap.postech.service_notificacoes.consumer;

import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import br.com.fiap.postech.service_notificacoes.dto.AgendamentoEventDTO;
import br.com.fiap.postech.service_notificacoes.service.LembreteAutomationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoConsumer {

    private final LembreteAutomationService automationService;

    public AgendamentoConsumer(LembreteAutomationService automationService) {
        this.automationService = automationService;
    }

    @RabbitListener(queues = RabbitMQFanoutConfig.LEMBRETE_QUEUE)
    public void receiveAgendamentoEvent(AgendamentoEventDTO event) {
        System.out.println("Evento de Agendamento recebido: " + event.getTipoEvento()
                + " para Agendamento ID: " + event.getAgendamentoId());

        automationService.handleAgendamentoEvent(event);
    }
}