package br.com.fiap.postech.service_notificacoes.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Lembrete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long agendamentoId;
    private Long pacienteId;
    private LocalDateTime dataHoraConsulta;
    private LocalDateTime dataHoraEnvio;
    private String status;

    public Lembrete() {}

    public Long getId() { return id; }
    public Long getAgendamentoId() { return agendamentoId; }
    public Long getPacienteId() { return pacienteId; }
    public LocalDateTime getDataHoraConsulta() { return dataHoraConsulta; }
    public LocalDateTime getDataHoraEnvio() { return dataHoraEnvio; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setAgendamentoId(Long agendamentoId) { this.agendamentoId = agendamentoId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public void setDataHoraConsulta(LocalDateTime dataHoraConsulta) { this.dataHoraConsulta = dataHoraConsulta; }
    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) { this.dataHoraEnvio = dataHoraEnvio; }
    public void setStatus(String status) { this.status = status; }
}