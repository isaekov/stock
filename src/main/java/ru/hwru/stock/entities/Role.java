package ru.hwru.stock.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

@Entity(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String role;

    @Override
    public String getAuthority() {
        return role;
    }

    public UUID getId() {
        return id;
    }

    public Role setId(UUID id) {
        this.id = id;
        return this;
    }

    public Role setRole(String role) {
        this.role = role;
        return this;
    }
}
