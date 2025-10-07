package br.com.fiap.postech.service_auth.exceptions;

public class RoleComUsuarioException extends RuntimeException {
    private static final String message = "Role [id=%d] possui usuário vinculado.";

    public RoleComUsuarioException(Long id) {
        super(message.formatted(id));
    }
}
