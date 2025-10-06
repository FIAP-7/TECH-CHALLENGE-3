package br.com.fiap.postech.service_auth.mapper;

import br.com.fiap.postech.service_auth.controllers.dto.RoleDTO;
import br.com.fiap.postech.service_auth.entities.Role;

public class RoleMapper {

    public static Role toEntity(RoleDTO.CreateRequest request) {
        if (request == null) return null;

        Role role = new Role();
        role.setName(request.name());

        return role;
    }

    public static Role toEntity(RoleDTO.UpdateRequest request) {
        if (request == null) return null;

        Role role = new Role();
        role.setId(request.id());
        role.setName(request.name());

        return role;
    }

    public static Role toEntity(RoleDTO.roleDTO request) {
        if (request == null) return null;

        Role role = new Role();
        role.setId(request.id());
        role.setName(request.name());

        return role;
    }

    public static RoleDTO.Response toResponse(Role entity) {
        if (entity == null) return null;

        return new RoleDTO.Response(
                entity.getId(),
                entity.getName()
        );
    }

}
