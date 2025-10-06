package br.com.fiap.postech.service_auth.exceptions;

public class UserJaExisteException extends RuntimeException {
    public UserJaExisteException() {
        super("Usuário já cadastrado");
    }
}
