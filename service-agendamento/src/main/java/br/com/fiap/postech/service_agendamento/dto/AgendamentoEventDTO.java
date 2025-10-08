package br.com.fiap.postech.service_agendamento.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AgendamentoEventDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long agendamentoId;
    private Long pacienteId;
    private LocalDateTime dataHora;
    private String tipoEvento;

    public AgendamentoEventDTO() {}

    public AgendamentoEventDTO(Long agendamentoId, Long pacienteId, LocalDateTime dataHora, String tipoEvento) {
        this.agendamentoId = agendamentoId;
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.tipoEvento = tipoEvento;
    }

    public Long getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
}