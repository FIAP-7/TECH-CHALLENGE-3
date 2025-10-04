package br.com.fiap.postech.service_agendamento.controllers;

import br.com.fiap.postech.service_agendamento.entities.Agendamento;
import br.com.fiap.postech.service_agendamento.services.AgendamentoService;
import br.com.fiap.postech.service_agendamento.controllers.dto.AgendamentoDTO;
import br.com.fiap.postech.service_agendamento.mapper.AgendamentoMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService service;

    public AgendamentoController(AgendamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AgendamentoDTO.Response> criar(@RequestBody AgendamentoDTO.CreateRequest request) {
        Agendamento novo = AgendamentoMapper.toEntity(request);
        Agendamento salvo = service.criar(novo);
        // TODO: Publicar mensagem no RabbitMQ aqui
        return ResponseEntity.created(URI.create("/agendamentos/" + salvo.getId()))
                .body(AgendamentoMapper.toResponse(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO.Response> atualizar(@PathVariable("id") Long id,
                                                             @RequestBody AgendamentoDTO.UpdateRequest request) {
        Agendamento atualizacao = AgendamentoMapper.toEntity(request);
        Agendamento atualizado = service.atualizar(id, atualizacao);
        // TODO: Publicar mensagem no RabbitMQ aqui
        return ResponseEntity.ok(AgendamentoMapper.toResponse(atualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO.Response> buscarPorId(@PathVariable("id") Long id) {
        Agendamento a = service.buscarPorId(id);
        return ResponseEntity.ok(AgendamentoMapper.toResponse(a));
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<AgendamentoDTO.Response>> listarPorPaciente(@PathVariable("pacienteId") Long pacienteId) {
        List<AgendamentoDTO.Response> lista = service.listarPorPaciente(pacienteId)
                .stream().map(AgendamentoMapper::toResponse).toList();
        return ResponseEntity.ok(lista);
    }

    // TODO: Método adicionado somente para auxiliar no desenvolvimento, remover antes da entrega final
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable("id") Long id) {
        service.remover(id);
    }
}
