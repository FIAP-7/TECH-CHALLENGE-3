package br.com.fiap.postech.service_notificacoes.controllers;

import br.com.fiap.postech.service_notificacoes.dto.LembreteDTO;
import br.com.fiap.postech.service_notificacoes.service.LembreteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lembretes")
public class LembreteController {

    private final LembreteService lembreteService;

    public LembreteController(LembreteService lembreteService) {
        this.lembreteService = lembreteService;
    }

    @PostMapping
    public ResponseEntity<LembreteDTO> criarLembrete(@RequestBody LembreteDTO dto) {
        LembreteDTO novoLembrete = lembreteService.criar(dto);
        return ResponseEntity.created(URI.create("/lembretes/" + novoLembrete.getId())).body(novoLembrete);
    }

    @GetMapping
    public ResponseEntity<List<LembreteDTO>> listarTodos() {
        return ResponseEntity.ok(lembreteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LembreteDTO> buscarPorId(@PathVariable Long id) {
        LembreteDTO lembrete = lembreteService.buscarPorId(id);
        return ResponseEntity.ok(lembrete);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LembreteDTO> atualizarLembrete(@PathVariable Long id, @RequestBody LembreteDTO dto) {
        LembreteDTO atualizado = lembreteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerLembrete(@PathVariable Long id) {
        lembreteService.remover(id);
        return ResponseEntity.noContent().build();
    }
}