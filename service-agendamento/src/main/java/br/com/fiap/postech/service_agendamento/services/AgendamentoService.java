package br.com.fiap.postech.service_agendamento.services;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.postech.service_agendamento.repositories.AgendamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository repository;

    public AgendamentoService(AgendamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Agendamento criar(Agendamento agendamento) {
        agendamento.setId(null);
        return repository.save(agendamento);
    }

    @Transactional
    public Agendamento atualizar(Long id, Agendamento atualizacao) {
        Agendamento existente = buscarPorId(id);
        if (atualizacao.getDataHora() != null) existente.setDataHora(atualizacao.getDataHora());
        if (atualizacao.getStatus() != null) existente.setStatus(atualizacao.getStatus());
        if (atualizacao.getMotivo() != null) existente.setMotivo(atualizacao.getMotivo());
        if (atualizacao.getPacienteId() != null) existente.setPacienteId(atualizacao.getPacienteId());
        if (atualizacao.getMedicoId() != null) existente.setMedicoId(atualizacao.getMedicoId());
        return repository.save(existente);
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
    }
}
