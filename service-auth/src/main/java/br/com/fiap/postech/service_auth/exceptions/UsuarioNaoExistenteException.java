package br.com.fiap.postech.service_auth.exceptions;

public class UsuarioNaoExistenteException extends RuntimeException {
    private static final String message = "Usuario [id=%d] encontrado";

    public UsuarioNaoExistenteException(Long id) {
        super(message.formatted(id));
    }
}
