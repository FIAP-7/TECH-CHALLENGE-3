package br.com.fiap.postech.service_auth.services;

import br.com.fiap.postech.service_auth.entities.Role;
import br.com.fiap.postech.service_auth.entities.Usuario;
import br.com.fiap.postech.service_auth.exceptions.RoleNaoExistenteException;
import br.com.fiap.postech.service_auth.exceptions.UserJaExisteException;
import br.com.fiap.postech.service_auth.exceptions.UsuarioNaoExistenteException;
import br.com.fiap.postech.service_auth.repositories.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RoleService roleService) {
        this.usuarioRepository = usuarioRepository;
        this.roleService = roleService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario criar(Usuario usuario) {
        var userOptional = usuarioRepository.findUsuarioByUsername(usuario.getUsername());

        if (userOptional.isPresent()){
            throw new UserJaExisteException();
        }

        if(usuario.getRole() == null){
            usuario.setRole(roleService.buscarPorId(usuario.getRole().getId()));
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));


        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario usuario) {
        var existente = usuarioRepository.findById(usuario.getId()).orElseThrow(() -> new UsuarioNaoExistenteException(id));

        if(usuario.getUsername() != null) existente.setUsername(usuario.getUsername());

        if(usuario.getPassword() != null) {
            if(!passwordEncoder.matches(usuario.getPassword(), existente.getPassword())){
                existente.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        if(usuario.getRole() != null) existente.setRole(usuario.getRole());

        existente.setActive(usuario.isActive());

        return usuarioRepository.save(existente);
    }

    public Usuario findUsuarioByUsername(String username) {
        return usuarioRepository.findUsuarioByUsername(username).orElse(null);
    }

    public void inativarUser(Long id){
        var user = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoExistenteException(id));

        user.setActive(false);

        usuarioRepository.save(user);
    }

    public Usuario atualizarRole(Long id, Role roleAtualizar) {
        var usuario = usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoExistenteException(id));

        var role = roleService.buscarPorId(roleAtualizar.getId());

        if(role == null){
            throw new RoleNaoExistenteException(roleAtualizar.getId());
        }

        usuario.setRole(role);

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String username, String password) {
        Optional<Usuario> usuario = usuarioRepository.findUsuarioByUsername(username);

        if(usuario.isPresent()){
            if(passwordEncoder.matches(password, usuario.get().getPassword())){
                return usuario;
            }
        }

        return Optional.empty();
    }

}
