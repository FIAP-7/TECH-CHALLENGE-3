package br.com.fiap.postech.service_historico.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import br.com.fiap.postech.service_historico.document.AgendamentoDocument;
import br.com.fiap.postech.service_historico.document.PessoaDocument;
import br.com.fiap.postech.service_historico.dto.AgendamentoCompletoDTO;
import br.com.fiap.postech.service_historico.repository.ConsultaRepository;

@Component
public class HistoricoConsumer {
    private final Logger log = LoggerFactory.getLogger(HistoricoConsumer.class);

    private final ConsultaRepository repository;

    public HistoricoConsumer(ConsultaRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = "historico_queue")
    public void receiveHistoricoNotification(AgendamentoCompletoDTO message) {
        repository.save(convertToDocument(message));
        log.info("Notificação recebida: {}", message);
    }

    private AgendamentoDocument convertToDocument(AgendamentoCompletoDTO dto) {
        PessoaDocument paciente = new PessoaDocument(
                dto.paciente().id(),
                dto.paciente().nome(),
                dto.paciente().email());
        PessoaDocument medico = new PessoaDocument(
                dto.medico().id(),
                dto.medico().nome(),
                dto.medico().email());
        return new AgendamentoDocument(
                dto.id(),
                paciente,
                medico,
                dto.dataHora(),
                dto.motivo(),
                dto.status());
    }
}
