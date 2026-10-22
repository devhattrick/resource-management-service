package com.company.resource_management_service.user.repository;

import com.company.resource_management_service.user.entity.AppUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<AppUser> findByEmailIgnoreCase(String email);

    @EntityGraph(attributePaths = {"roles", "roles.permissions"})
    Optional<AppUser> findWithRolesById(UUID id);
}
