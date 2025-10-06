package br.com.fiap.postech.service_auth.repositories;

import br.com.fiap.postech.service_auth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
