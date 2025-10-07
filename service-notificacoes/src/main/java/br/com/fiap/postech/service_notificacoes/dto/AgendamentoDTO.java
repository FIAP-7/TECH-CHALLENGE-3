package br.com.fiap.postech.service_notificacoes.dto;

import java.time.OffsetDateTime;

public record AgendamentoDTO(Long id, 
PessoaDTO paciente, 
PessoaDTO medico, 
OffsetDateTime dataHora, 
String motivo, 
String status) {

}
