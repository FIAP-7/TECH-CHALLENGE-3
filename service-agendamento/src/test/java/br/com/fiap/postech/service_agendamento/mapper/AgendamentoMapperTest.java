package br.com.fiap.postech.service_agendamento.mapper;

import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoDTO;
import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class AgendamentoMapperTest {

    @Test
    void toEntity_fromCreateRequest_mapsAllFields() {
        OffsetDateTime now = OffsetDateTime.parse("2025-01-01T10:00:00Z");
        AgendamentoDTO.CreateRequest req = new AgendamentoDTO.CreateRequest(1L, 2L, now, "Motivo");

        Agendamento ag = AgendamentoMapper.toEntity(req);

        assertNotNull(ag);
        assertEquals(1L, ag.getPacienteId());
        assertEquals(2L, ag.getMedicoId());
        assertEquals(now, ag.getDataHora());
        assertEquals("Motivo", ag.getMotivo());
        assertEquals(Agendamento.Status.AGENDADO, ag.getStatus());
    }

    @Test
    void toEntity_fromUpdateRequest_mapsAllFields() {
        OffsetDateTime later = OffsetDateTime.parse("2025-01-02T11:30:00Z");
        AgendamentoDTO.UpdateRequest req = new AgendamentoDTO.UpdateRequest(later, Agendamento.Status.REMARCADO, "Novo motivo", 10L, 20L);

        Agendamento ag = AgendamentoMapper.toEntity(req);

        assertNotNull(ag);
        assertEquals(later, ag.getDataHora());
        assertEquals(Agendamento.Status.REMARCADO, ag.getStatus());
        assertEquals("Novo motivo", ag.getMotivo());
        assertEquals(10L, ag.getPacienteId());
        assertEquals(20L, ag.getMedicoId());
    }

    @Test
    void toResponse_mapsAllFields() {
        OffsetDateTime when = OffsetDateTime.parse("2025-01-03T09:15:00Z");
        Agendamento ag = new Agendamento();
        ag.setId(99L);
        ag.setPacienteId(3L);
        ag.setMedicoId(4L);
        ag.setDataHora(when);
        ag.setMotivo("Checkup");
        ag.setStatus(Agendamento.Status.REALIZADO);

        AgendamentoDTO.Response resp = AgendamentoMapper.toResponse(ag);

        assertNotNull(resp);
        assertEquals(99L, resp.id());
        assertEquals(3L, resp.pacienteId());
        assertEquals(4L, resp.medicoId());
        assertEquals(when, resp.dataHora());
        assertEquals("Checkup", resp.motivo());
        assertEquals(Agendamento.Status.REALIZADO, resp.status());
    }
}