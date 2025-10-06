package br.com.fiap.postech.service_auth.repositories;

import br.com.fiap.postech.service_auth.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findUsuarioByUsername(String username);

    long countUsuarioByRole_Id(Long roleId);

}
