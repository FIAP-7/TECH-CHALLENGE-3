package br.com.fiap.postech.service_auth.controllers;

import br.com.fiap.postech.service_auth.controllers.dto.RoleDTO;
import br.com.fiap.postech.service_auth.entities.Role;
import br.com.fiap.postech.service_auth.mapper.RoleMapper;
import br.com.fiap.postech.service_auth.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleDTO.Response> criar(@RequestBody RoleDTO.CreateRequest roleDTO) {
        Role novo = RoleMapper.toEntity(roleDTO);

        Role criar = roleService.criar(novo);

        return ResponseEntity.created(URI.create("/role/" + criar.getId())).body(RoleMapper.toResponse(criar));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO.Response> atualizar(@PathVariable("id") Long id, @RequestBody RoleDTO.UpdateRequest roleDTO) {
        Role novo = RoleMapper.toEntity(roleDTO);
        Role atualizado = roleService.atualizar(id, novo);
        return ResponseEntity.ok().body(RoleMapper.toResponse(atualizado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO.Response> buscar(@PathVariable("id") Long id) {
        Role role = roleService.buscarPorId(id);

        return ResponseEntity.ok().body(RoleMapper.toResponse(role));
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO.Response>> buscarTodos() {
        List<Role> roles = roleService.buscarTodos();

        List<RoleDTO.Response> collect = roles.stream().map(role -> RoleMapper.toResponse(role)).collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        roleService.deletar(id);

        return ResponseEntity.ok().build();
    }
}
