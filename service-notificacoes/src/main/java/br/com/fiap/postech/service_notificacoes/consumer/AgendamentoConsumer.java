package br.com.fiap.postech.service_notificacoes.consumer;

import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import br.com.fiap.postech.service_notificacoes.dto.AgendamentoEventDTO;
import br.com.fiap.postech.service_notificacoes.service.LembreteDelayService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoConsumer {

    private final LembreteDelayService delayService;

    public AgendamentoConsumer(LembreteDelayService delayService) {
        this.delayService = delayService;
    }

    /**
     * Ouve a fila de recebimento inicial.
     * Esta fila está ligada ao Fanout Exchange, que recebe a mensagem do service-agendamento.
     */
    @RabbitListener(queues = RabbitMQFanoutConfig.INITIAL_RECEIVE_QUEUE)
    public void receiveAgendamentoEvent(AgendamentoEventDTO event) {
        System.out.println("Evento de Agendamento recebido (Inicial). Acionando a lógica de atraso...");

        delayService.agendarLembrete(event);
    }
}