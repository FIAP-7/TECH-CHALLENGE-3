package br.com.fiap.postech.service_agendamento.services;

import br.com.fiap.postech.service_agendamento.dto.AgendamentoEventDTO;
import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.postech.service_agendamento.repositories.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final AgendamentoPublisherService publisherService;

    public AgendamentoService(AgendamentoRepository repository, AgendamentoPublisherService publisherService) {
        this.repository = repository;
        this.publisherService = publisherService;
    }

    @Transactional
    public Agendamento criar(Agendamento agendamento) {
        agendamento.setId(null);
        Agendamento salvo = repository.save(agendamento);

        LocalDateTime dataHoraSemOffset = salvo.getDataHora().toLocalDateTime();

        AgendamentoEventDTO event = new AgendamentoEventDTO(
                salvo.getId(),
                salvo.getPacienteId(),
                dataHoraSemOffset,
                "CRIACAO"
        );
        publisherService.publishAgendamentoEvent(event);

        return salvo;
    }

    @Transactional
    public Agendamento atualizar(Long id, Agendamento atualizacao) {
        Agendamento existente = buscarPorId(id);
        if (atualizacao.getDataHora() != null) existente.setDataHora(atualizacao.getDataHora());
        if (atualizacao.getStatus() != null) existente.setStatus(atualizacao.getStatus());
        if (atualizacao.getMotivo() != null) existente.setMotivo(atualizacao.getMotivo());
        if (atualizacao.getPacienteId() != null) existente.setPacienteId(atualizacao.getPacienteId());
        if (atualizacao.getMedicoId() != null) existente.setMedicoId(atualizacao.getMedicoId());
        Agendamento atualizado = repository.save(existente);

        LocalDateTime dataHoraSemOffset = atualizado.getDataHora().toLocalDateTime();

        AgendamentoEventDTO event = new AgendamentoEventDTO(
                atualizado.getId(),
                atualizado.getPacienteId(),
                dataHoraSemOffset,
                "ATUALIZACAO"
        );
        publisherService.publishAgendamentoEvent(event);

        return atualizado;
    }

    @Transactional(readOnly = true)
    public Agendamento buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Agendamento não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarPorPaciente(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    // TODO: Método somente para auxiliar no desenvolvimento, remover antes da entrega
    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Agendamento não encontrado: " + id);
        }
        repository.deleteById(id);

        AgendamentoEventDTO event = new AgendamentoEventDTO(
                id,
                null,
                null,
                "CANCELAMENTO"
        );
        publisherService.publishAgendamentoEvent(event);
    }
}
