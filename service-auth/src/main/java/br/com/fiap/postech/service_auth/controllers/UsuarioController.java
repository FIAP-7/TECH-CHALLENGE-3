package br.com.fiap.postech.service_auth.controllers;

import br.com.fiap.postech.service_auth.controllers.dto.UsuarioDTO;
import br.com.fiap.postech.service_auth.entities.Usuario;
import br.com.fiap.postech.service_auth.mapper.UsuarioMapper;
import br.com.fiap.postech.service_auth.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/usuario")
@Tag(name = "Usuario", description = "Endpoints para gerenciar os Usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERO')")
    public ResponseEntity<UsuarioDTO.Response> create(@RequestBody UsuarioDTO.CreateRequest usuarioRequest) {
        Usuario novo = UsuarioMapper.toEntity(usuarioRequest);

        var criar = usuarioService.criar(novo);

        return ResponseEntity.created(URI.create("/usuario/" + criar.getId())).body(UsuarioMapper.toResponse(criar));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERO')")
    public ResponseEntity<UsuarioDTO.Response> update(@PathVariable("id") Long id, @RequestBody UsuarioDTO.UpdateRequest usuarioRequest) {
        Usuario novo = UsuarioMapper.toEntity(usuarioRequest);
        var atualizado = usuarioService.atualizar(id, novo);

        return ResponseEntity.ok().body(UsuarioMapper.toResponse(atualizado));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERO')")
    public ResponseEntity<UsuarioDTO.Response> buscar(@PathVariable("username") String username) {
        Usuario user = usuarioService.findUsuarioByUsername(username);

        return ResponseEntity.ok().body(UsuarioMapper.toResponse(user));
    }

    @PostMapping("/inativar/{id}")
    @PreAuthorize("hasAnyRole('MEDICO', 'ENFERMERO')")
    public ResponseEntity<Void> inativar(@PathVariable("id") Long id) {
        usuarioService.inativarUser(id);

        return ResponseEntity.ok().build();
    }
}
