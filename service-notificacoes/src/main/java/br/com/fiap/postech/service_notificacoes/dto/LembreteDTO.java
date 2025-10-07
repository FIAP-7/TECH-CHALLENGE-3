package br.com.fiap.postech.service_notificacoes.dto;

import java.time.LocalDateTime;

public class LembreteDTO {
    private Long id;
    private Long pacienteId;
    private Long agendamentoId;
    private LocalDateTime dataHoraConsulta;
    private LocalDateTime dataHoraEnvio;
    private String status;

    public LembreteDTO() {}

    public LembreteDTO(Long id, Long pacienteId, Long agendamentoId, LocalDateTime dataHoraConsulta, LocalDateTime dataHoraEnvio, String status) {
        this.id = id;
        this.pacienteId = pacienteId;
        this.agendamentoId = agendamentoId;
        this.dataHoraConsulta = dataHoraConsulta;
        this.dataHoraEnvio = dataHoraEnvio;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public Long getAgendamentoId() { return agendamentoId; }
    public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
    public LocalDateTime getDataHoraConsulta() { return dataHoraConsulta; }
    public void setDataHoraConsulta(LocalDateTime dataHoraConsulta) { this.dataHoraConsulta = dataHoraConsulta; }
    public LocalDateTime getDataHoraEnvio() { return dataHoraEnvio; }
    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) { this.dataHoraEnvio = dataHoraEnvio; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}