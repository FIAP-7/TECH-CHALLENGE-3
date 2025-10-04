package br.com.fiap.postech.service_agendamento.mapper;

import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoDTO;
import br.com.fiap.postech.service_agendamento.entities.Agendamento;

public class AgendamentoMapper {

    public static Agendamento toEntity(AgendamentoDTO.CreateRequest request) {
        if (request == null) return null;
        Agendamento agendamento = new Agendamento();
        agendamento.setPacienteId(request.pacienteId());
        agendamento.setMedicoId(request.medicoId());
        agendamento.setDataHora(request.dataHora());
        agendamento.setMotivo(request.motivo());
        return agendamento;
    }

    public static Agendamento toEntity(AgendamentoDTO.UpdateRequest request) {
        if (request == null) return null;
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(request.dataHora());
        agendamento.setStatus(request.status());
        agendamento.setMotivo(request.motivo());
        agendamento.setPacienteId(request.pacienteId());
        agendamento.setMedicoId(request.medicoId());
        return agendamento;
    }

    public static AgendamentoDTO.Response toResponse(Agendamento agendamento) {
        if (agendamento == null) return null;
        return new AgendamentoDTO.Response(
                agendamento.getId(),
                agendamento.getPacienteId(),
                agendamento.getMedicoId(),
                agendamento.getDataHora(),
                agendamento.getMotivo(),
                agendamento.getStatus()
        );
    }
}
