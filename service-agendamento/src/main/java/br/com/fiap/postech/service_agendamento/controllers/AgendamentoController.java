package br.com.fiap.postech.service_agendamento.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoCompletoDTO;
import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoDTO;
import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoDTO.Response;
import br.com.fiap.postech.service_agendamento.controllers.dto.PessoaDTO;
import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.mapper.AgendamentoMapper;
import br.com.fiap.postech.service_agendamento.services.AgendamentoService;
import br.com.fiap.postech.service_notificacoes.config.RabbitMQFanoutConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/agendamentos")
@Tag(name = "Agendamentos", description = "Endpoints para gerenciar agendamentos de consultas")
public class AgendamentoController {

    private final AgendamentoService service;
    private final RabbitTemplate rabbitTemplate;

    public AgendamentoController(AgendamentoService service, RabbitTemplate rabbitTemplate) {
        this.service = service;
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping
    @Operation(summary = "Criar novo agendamento", description = "Cria um agendamento para um paciente e médico na data/hora informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Agendamento criado com sucesso",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.Response.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMERO')")
    public ResponseEntity<AgendamentoDTO.Response> criar(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para criação do agendamento")
                                                         @RequestBody AgendamentoDTO.CreateRequest request) {
        Agendamento novo = AgendamentoMapper.toEntity(request);
        Agendamento salvo = service.criar(novo);
        
        publicarMensagem(AgendamentoMapper.toResponse(salvo));
        
        return ResponseEntity.created(URI.create("/agendamentos/" + salvo.getId()))
                .body(AgendamentoMapper.toResponse(salvo));
    }

    private void publicarMensagem(Response agendamento) {
        PessoaDTO paciente = new PessoaDTO(1L, "Nome Paciente", "teste@teste.com");
        PessoaDTO medico = new PessoaDTO(2L, "Nome Medico", "medico@email.com");
        AgendamentoCompletoDTO dto = new AgendamentoCompletoDTO(agendamento.id(), 
	        paciente, 
	        medico, 
	        agendamento.dataHora().toString(), 
	        agendamento.motivo(),  
	        agendamento.status().toString());

        rabbitTemplate.convertAndSend(
                        RabbitMQFanoutConfig.EXCHANGE_NAME,
                        "",
                        dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar agendamento", description = "Atualiza campos do agendamento existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento atualizado",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.Response.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN','MEDICO')")
    public ResponseEntity<AgendamentoDTO.Response> atualizar(@Parameter(description = "ID do agendamento a ser atualizado")
                                                             @PathVariable("id") Long id,
                                                             @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados para atualização do agendamento")
                                                             @RequestBody AgendamentoDTO.UpdateRequest request) {
        Agendamento atualizacao = AgendamentoMapper.toEntity(request);
        Agendamento atualizado = service.atualizar(id, atualizacao);
        
        publicarMensagem(AgendamentoMapper.toResponse(atualizado));
        
        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar agendamento por ID", description = "Retorna os detalhes do agendamento pelo seu ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Agendamento encontrado",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.Response.class))),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<AgendamentoDTO.Response> buscarPorId(@Parameter(description = "ID do agendamento")
                                                               @PathVariable("id") Long id) {
        Agendamento a = service.buscarPorId(id);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(a));
    }

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Listar agendamentos por paciente", description = "Lista todos os agendamentos associados ao paciente informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de agendamentos",
                    content = @Content(schema = @Schema(implementation = AgendamentoDTO.Response.class)))
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMERO', 'PACIENTE')")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarPorPaciente(@Parameter(description = "ID do paciente")
                                                                           @PathVariable("pacienteId") Long pacienteId) {
        List<AgendamentoDTO.Response> lista = service.listarPorPaciente(pacienteId)
                .stream().map(AgendamentoMapper::toResponse).toList();
        return ResponseEntity.ok(lista);
    }

    // TODO: Método adicionado somente para auxiliar no desenvolvimento, remover antes da entrega final
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover agendamento", description = "Remove um agendamento existente pelo ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Agendamento removido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Agendamento não encontrado", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public void remover(@Parameter(description = "ID do agendamento a ser removido")
                        @PathVariable("id") Long id) {
        service.remover(id);
    }
}
