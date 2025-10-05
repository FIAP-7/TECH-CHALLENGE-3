package br.com.fiap.postech.service_auth.exceptions;

public class RoleNaoExistenteException extends RuntimeException {
    private static final String message = "Role [id=%d] encontrada";

    public RoleNaoExistenteException(Long id) {
        super(message.formatted(id));
    }
}
