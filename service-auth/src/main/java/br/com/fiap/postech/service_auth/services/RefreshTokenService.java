package br.com.fiap.postech.service_auth.services;

import br.com.fiap.postech.service_auth.entities.RefreshToken;
import br.com.fiap.postech.service_auth.entities.Usuario;
import br.com.fiap.postech.service_auth.exceptions.TokenExpiradoException;
import br.com.fiap.postech.service_auth.repositories.RefreshTokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken criarRefreshToken(Usuario usuario) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUsuario(usuario);
        refreshToken.setExpiryDate(Instant.now().plusSeconds(2 * 24 * 60 * 60));
        refreshToken.setToken(UUID.randomUUID().toString());

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> buscarToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verificarExpiracao(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new TokenExpiradoException();
        }

        return refreshToken;
    }

    public int deleteRefreshTokenUsuario(Usuario usuario) {
        return refreshTokenRepository.deleteByUsuario(usuario);
    }
}
