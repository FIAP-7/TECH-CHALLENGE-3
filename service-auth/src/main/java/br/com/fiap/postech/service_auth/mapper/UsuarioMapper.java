package br.com.fiap.postech.service_auth.mapper;

import br.com.fiap.postech.service_auth.controllers.dto.RoleDTO;
import br.com.fiap.postech.service_auth.controllers.dto.UsuarioDTO;
import br.com.fiap.postech.service_auth.entities.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioDTO.CreateRequest createRequest) {
        if (createRequest == null) return null;

        Usuario entity = new Usuario();

        entity.setUsername(createRequest.username());
        entity.setPassword(createRequest.password());
        entity.setRole(RoleMapper.toEntity(createRequest.role()));
        entity.setActive(true);

        return entity;
    }

    public static Usuario toEntity(UsuarioDTO.UpdateRequest updateRequest) {
        if (updateRequest == null) return null;

        Usuario entity = new Usuario();

        entity.setId(updateRequest.id());
        entity.setUsername(updateRequest.username());
        entity.setPassword(updateRequest.password());
        entity.setRole(RoleMapper.toEntity(updateRequest.role()));
        entity.setActive(updateRequest.active());

        return entity;
    }

    public static UsuarioDTO.Response toResponse(Usuario entity) {
        if (entity == null) return null;

        return new UsuarioDTO.Response(
                entity.getId(),
                entity.getUsername(),
                entity.isActive(),
                RoleMapper.toResponse(entity.getRole())
        );
    }
}
