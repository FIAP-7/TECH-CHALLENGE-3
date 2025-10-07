package br.com.fiap.postech.service_agendamento.controllers.dto;

public record AgendamentoCompletoDTO(Long id,
        PessoaDTO paciente,
        PessoaDTO medico,
        String dataHora,
        String motivo,
        String status) {

}
