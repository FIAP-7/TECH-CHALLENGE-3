package br.com.fiap.postech.service_agendamento.services;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.postech.service_agendamento.repositories.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AgendamentoServiceTest {

    private AgendamentoRepository repository;
    private AgendamentoService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AgendamentoRepository.class);
        service = new AgendamentoService(repository);
    }

    @Test
    void criar_setsIdNull_andSaves() {
        Agendamento input = new Agendamento();
        input.setId(123L);
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Agendamento saved = service.criar(input);

        assertNull(saved.getId());
        verify(repository).save(input);
    }

    @Test
    void atualizar_appliesNonNullFields_andSaves() {
        Agendamento existente = new Agendamento();
        existente.setId(1L);
        existente.setPacienteId(10L);
        existente.setMedicoId(20L);
        existente.setMotivo("Antigo");
        existente.setStatus(Agendamento.Status.AGENDADO);
        existente.setDataHora(OffsetDateTime.parse("2025-01-01T10:00:00Z"));

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Agendamento atualizacao = new Agendamento();
        atualizacao.setPacienteId(11L);
        atualizacao.setMedicoId(22L);
        atualizacao.setMotivo("Novo");
        atualizacao.setStatus(Agendamento.Status.REMARCADO);
        atualizacao.setDataHora(OffsetDateTime.parse("2025-01-02T11:30:00Z"));

        Agendamento atualizado = service.atualizar(1L, atualizacao);

        assertEquals(11L, atualizado.getPacienteId());
        assertEquals(22L, atualizado.getMedicoId());
        assertEquals("Novo", atualizado.getMotivo());
        assertEquals(Agendamento.Status.REMARCADO, atualizado.getStatus());
        assertEquals(OffsetDateTime.parse("2025-01-02T11:30:00Z"), atualizado.getDataHora());
        verify(repository).save(existente);
    }

    @Test
    void buscarPorId_found_returnsEntity() {
        Agendamento ag = new Agendamento();
        ag.setId(7L);
        when(repository.findById(7L)).thenReturn(Optional.of(ag));

        Agendamento result = service.buscarPorId(7L);
        assertEquals(7L, result.getId());
    }

    @Test
    void buscarPorId_missing_throwsNotFound() {
        when(repository.findById(9L)).thenReturn(Optional.empty());
        RecursoNaoEncontradoException ex = assertThrows(RecursoNaoEncontradoException.class, () -> service.buscarPorId(9L));
        assertTrue(ex.getMessage().contains("9"));
    }

    @Test
    void listarPorPaciente_delegatesToRepository() {
        when(repository.findByPacienteId(123L)).thenReturn(List.of(new Agendamento(), new Agendamento()));
        List<Agendamento> list = service.listarPorPaciente(123L);
        assertEquals(2, list.size());
        verify(repository).findByPacienteId(123L);
    }

    @Test
    void remover_exists_deletes() {
        when(repository.existsById(5L)).thenReturn(true);
        service.remover(5L);
        verify(repository).deleteById(5L);
    }

    @Test
    void remover_missing_throwsNotFound() {
        when(repository.existsById(6L)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> service.remover(6L));
        verify(repository, never()).deleteById(any());
    }
}
