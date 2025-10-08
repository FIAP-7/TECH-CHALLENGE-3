package br.com.fiap.postech.service_auth.exceptions;

public class TokenExpiradoException extends RuntimeException {
    public TokenExpiradoException() {
        super("Refresh Token expirado, realize um novo username.");
    }
}
