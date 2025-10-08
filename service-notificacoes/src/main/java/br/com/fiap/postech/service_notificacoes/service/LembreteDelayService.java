package br.com.fiap.postech.service_notificacoes.service;

import br.com.fiap.postech.service_notificacoes.dto.AgendamentoEventDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LembreteDelayService {

    private static final Logger log = LoggerFactory.getLogger(LembreteDelayService.class);

    /**
     * Serviço placeholder para evitar erro de compilação e permitir evolução futura.
     * Neste momento, a lógica de envio de lembretes é realizada diretamente ao receber
     * mensagens na fila email_queue através do EmailConsumer/NotificationPublisherService.
     */
    public void agendarLembrete(AgendamentoEventDTO event) {
        log.info("[LembreteDelayService] Recebido evento de agendamento {}. Nenhuma ação de delay configurada no momento.",
                event != null ? event.getAgendamentoId() : null);
    }
}
