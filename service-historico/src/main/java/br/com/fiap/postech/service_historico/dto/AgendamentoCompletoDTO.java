package br.com.fiap.postech.service_historico.dto;

public record AgendamentoCompletoDTO(Long id, 
	PessoaDTO paciente, 
	PessoaDTO medico, 
	String dataHora, 
	String motivo, 
	String status) {

}
