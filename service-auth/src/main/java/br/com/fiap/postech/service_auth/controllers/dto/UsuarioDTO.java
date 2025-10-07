package br.com.fiap.postech.service_auth.controllers.dto;

public class UsuarioDTO {

    public record CreateRequest(
            String username,
            String password,
            String name,
            String email,
            RoleDTO.roleDTO role
    ) {}

    public record UpdateRequest(
            Long id,
            String username,
            String password,
            String name,
            String email,           
            Boolean active,
            RoleDTO.roleDTO role
    ) {}

    public record Response(
            Long id,
            String username,
            Boolean active,
            RoleDTO.Response role
    ){}
}
