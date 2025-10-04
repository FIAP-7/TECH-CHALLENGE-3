package br.com.fiap.postech.service_agendamento.controllers.dto;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;

import java.time.OffsetDateTime;

public class AgendamentoDTO {

    public record CreateRequest(
            Long pacienteId,
            Long medicoId,
            OffsetDateTime dataHora,
            String motivo
    ) {}

    public record UpdateRequest(
            OffsetDateTime dataHora,
            Agendamento.Status status,
            String motivo,
            Long pacienteId,
            Long medicoId
    ) {}

    public record Response(
            Long id,
            Long pacienteId,
            Long medicoId,
            OffsetDateTime dataHora,
            String motivo,
            Agendamento.Status status
    ) {}
}
