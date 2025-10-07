package br.com.fiap.postech.service_historico.dto;

import java.time.OffsetDateTime;

public record AgendamentoCompletoDTO(Long id, 
PessoaDTO paciente, 
PessoaDTO medico, 
OffsetDateTime dataHora, 
String motivo, 
String status) {

}
