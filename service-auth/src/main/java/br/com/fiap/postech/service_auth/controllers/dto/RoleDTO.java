package br.com.fiap.postech.service_auth.controllers.dto;

public class RoleDTO {

    public record CreateRequest(String name){}
    public record UpdateRequest(Long id, String name){}
    public record Response(Long id, String name){}

    public record roleDTO(Long id, String name){}
}
