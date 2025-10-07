package br.com.fiap.postech.service_historico.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.OffsetDateTime;

@Document(collection = "agendamentos")
public class AgendamentoDocument {
    @Id
    private Long id;
    private PessoaDocument paciente;
    private PessoaDocument medico;
    private OffsetDateTime dataHora;
    private String motivo;
    private String status;

    public AgendamentoDocument() {}

    public AgendamentoDocument(Long id, PessoaDocument paciente, PessoaDocument medico, OffsetDateTime dataHora, String motivo, String status) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public PessoaDocument getPaciente() { return paciente; }
    public void setPaciente(PessoaDocument paciente) { this.paciente = paciente; }
    public PessoaDocument getMedico() { return medico; }
    public void setMedico(PessoaDocument medico) { this.medico = medico; }
    public OffsetDateTime getDataHora() { return dataHora; }
    public void setDataHora(OffsetDateTime dataHora) { this.dataHora = dataHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
