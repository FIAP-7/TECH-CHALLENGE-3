package br.com.fiap.postech.service_auth.repositories;

import br.com.fiap.postech.service_auth.entities.RefreshToken;
import br.com.fiap.postech.service_auth.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    int deleteByUsuario(Usuario usuario);

}
