package ru.hwru.stock.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.hwru.stock.entities.Role;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends CrudRepository<Role, UUID> {

    Optional<Role> findByRole(String role);
}
