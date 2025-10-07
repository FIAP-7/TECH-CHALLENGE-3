package br.com.fiap.postech.service_notificacoes.service;

import br.com.fiap.postech.service_notificacoes.dto.LembreteDTO;
import br.com.fiap.postech.service_notificacoes.entities.Lembrete;
import br.com.fiap.postech.service_notificacoes.repositories.LembreteRepository;
import br.com.fiap.postech.service_notificacoes.exceptions.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LembreteService {

    private final LembreteRepository repository;

    public LembreteService(LembreteRepository repository) {
        this.repository = repository;
    }

    private Lembrete toEntity(LembreteDTO dto) {
        Lembrete entity = new Lembrete();
        entity.setId(dto.getId());
        entity.setAgendamentoId(dto.getAgendamentoId());
        entity.setPacienteId(dto.getPacienteId());
        entity.setDataHoraConsulta(dto.getDataHoraConsulta());
        entity.setDataHoraEnvio(dto.getDataHoraEnvio());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private LembreteDTO toDTO(Lembrete entity) {
        return new LembreteDTO(
                entity.getId(),
                entity.getPacienteId(),
                entity.getAgendamentoId(),
                entity.getDataHoraConsulta(),
                entity.getDataHoraEnvio(),
                entity.getStatus()
        );
    }

    @Transactional
    public LembreteDTO criar(LembreteDTO dto) {
        Lembrete lembrete = toEntity(dto);
        lembrete.setId(null);
        lembrete = repository.save(lembrete);
        return toDTO(lembrete);
    }

    @Transactional
    public LembreteDTO atualizar(Long id, LembreteDTO dto) {
        Lembrete existente = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lembrete não encontrado: " + id));

        existente.setPacienteId(dto.getPacienteId());
        existente.setAgendamentoId(dto.getAgendamentoId());
        existente.setDataHoraConsulta(dto.getDataHoraConsulta());
        existente.setDataHoraEnvio(dto.getDataHoraEnvio());
        existente.setStatus(dto.getStatus());

        Lembrete atualizado = repository.save(existente);
        return toDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public LembreteDTO buscarPorId(Long id) {
        Lembrete lembrete = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Lembrete não encontrado: " + id));
        return toDTO(lembrete);
    }

    @Transactional(readOnly = true)
    public List<LembreteDTO> listarTodos() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public void remover(Long id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Lembrete não encontrado: " + id);
        }
        repository.deleteById(id);
    }
}