package br.com.fiap.postech.service_auth.controllers;

import br.com.fiap.postech.service_auth.controllers.dto.AuthDTO;
import br.com.fiap.postech.service_auth.controllers.dto.AuthTokenDTO;
import br.com.fiap.postech.service_auth.entities.RefreshToken;
import br.com.fiap.postech.service_auth.entities.Usuario;
import br.com.fiap.postech.service_auth.services.RefreshTokenService;
import br.com.fiap.postech.service_auth.services.TokenService;
import br.com.fiap.postech.service_auth.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para realizar autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final RefreshTokenService refreshTokenService;
    private final TokenService tokenService;

    public AuthController(
            UsuarioService usuarioService,
            TokenService tokenService,
            AuthenticationManager authenticationManager,
            RefreshTokenService refreshTokenService
    ) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(summary = "Login usuario")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO dto) {
        Optional<Usuario> user = usuarioService.login(dto.login(), dto.password());

        if (user.isPresent()) {
            String accessToken = tokenService.generateToken(user.get());
            RefreshToken refreshToken = refreshTokenService.criarRefreshToken(user.get());

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "token", refreshToken.getToken()
            ));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Validar token")
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validate(@RequestBody AuthTokenDTO token) {
        boolean tokenValid = tokenService.isTokenValid(token.token(), token.username());

        if (tokenValid) {
            return ResponseEntity.ok(tokenValid);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tokenValid);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> body) {
        String requestToken = body.get("token");

        RefreshToken refreshToken = refreshTokenService.buscarToken(requestToken)
                .map(refreshTokenService::verificarExpiracao)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        Usuario user = refreshToken.getUsuario();
        String novoTokenAcesso = tokenService.generateToken(user);

        return ResponseEntity.ok(Map.of(
                "accessToken", novoTokenAcesso
        ));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> body) {
        String username = body.get("username");

        Usuario user = usuarioService.findUsuarioByUsername(username);
        refreshTokenService.deleteRefreshTokenUsuario(user);

        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
