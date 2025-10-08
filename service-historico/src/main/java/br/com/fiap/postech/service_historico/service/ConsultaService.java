package br.com.fiap.postech.service_historico.service;

import br.com.fiap.postech.service_historico.document.AgendamentoDocument;
import br.com.fiap.postech.service_historico.repository.ConsultaRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private ConsultaRepository repository;

    public ConsultaService(ConsultaRepository repository) {
        this.repository = repository;
    }

    public List<AgendamentoDocument> findAll() {
        return repository.findAll();
    }

    public AgendamentoDocument save(AgendamentoDocument agendamento) {
        return repository.save(agendamento);
    }

    public List<AgendamentoDocument> findByDataHoraBefore(OffsetDateTime now) {
        return repository.findByDataHoraBefore(OffsetDateTime.now());
    }

    public List<AgendamentoDocument> findByPacienteId(Long pacienteId) {
        return repository.findByPacienteId(pacienteId);
    }

    public List<AgendamentoDocument> findByPacienteIdAndDataHoraBefore(Long pacienteId, OffsetDateTime now) {
        return repository.findByPacienteIdAndDataHoraBefore(pacienteId, OffsetDateTime.now());
    }

    public AgendamentoDocument findById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
