package br.com.fiap.postech.service_auth.services;

import br.com.fiap.postech.service_auth.entities.Role;
import br.com.fiap.postech.service_auth.exceptions.RoleComUsuarioException;
import br.com.fiap.postech.service_auth.repositories.RoleRepository;
import br.com.fiap.postech.service_auth.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    private final UsuarioRepository usuarioRepository;

    public RoleService(RoleRepository roleRepository, UsuarioRepository usuarioRepository) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Role> buscarTodos() {
        return roleRepository.findAll();
    }

    public Role buscarPorId(Long id){
        return roleRepository.findById(id).orElse(null);
    }

    public Role criar(Role role){
        return roleRepository.save(role);
    }

    public Role atualizar(Long id, Role atualizacao){
        var existente = buscarPorId(id);

        if(atualizacao.getName() != null) existente.setName(atualizacao.getName());

        return roleRepository.save(atualizacao);
    }

    public void deletar(Long id){
        if(usuarioRepository.countUsuarioByRole_Id(id) > 0){
            throw new RoleComUsuarioException(id);
        }

        roleRepository.deleteById(id);
    }
}
