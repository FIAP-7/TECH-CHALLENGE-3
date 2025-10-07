package br.com.fiap.postech.service_notificacoes.dto;

public record AgendamentoDTO(Long id, 
	PessoaDTO paciente, 
	PessoaDTO medico, 
	String dataHora, 
	String motivo, 
	String status) {
}
