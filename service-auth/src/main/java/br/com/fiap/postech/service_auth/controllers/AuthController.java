package br.com.fiap.postech.service_auth.controllers;

import br.com.fiap.postech.service_auth.controllers.dto.AuthDTO;
import br.com.fiap.postech.service_auth.entities.Usuario;
import br.com.fiap.postech.service_auth.services.TokenService;
import br.com.fiap.postech.service_auth.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.postech.service_auth.services.TokenService.TokenValidationResult;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Endpoints para realizar autenticação")
public class AuthController {

    private final UsuarioService usuarioService;

    private final TokenService tokenService;

    public AuthController(UsuarioService usuarioService, TokenService tokenService) {
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Login usuario")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthDTO dto) {
        Optional<Usuario> user = usuarioService.login(dto.login(), dto.password());

        if (user.isPresent()) {
            String token = tokenService.generateToken(user.get());
            return ResponseEntity.ok(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @Operation(summary = "Validar token")
    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResult> validate(@RequestBody String token) {
        TokenValidationResult result = tokenService.validateToken(token);

        if (result.isValid()) {
            return ResponseEntity.ok(result);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
    }
}
