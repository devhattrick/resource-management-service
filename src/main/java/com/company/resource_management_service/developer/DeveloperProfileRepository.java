package com.company.resource_management_service.developer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeveloperProfileRepository extends JpaRepository<DeveloperProfile, UUID> {

    boolean existsByUserId(UUID userId);

    java.util.Optional<DeveloperProfile> findByUserId(UUID userId);

    @EntityGraph(attributePaths = {"user", "roles", "roles.jobRole", "languages", "languages.language"})
    Optional<DeveloperProfile> findWithDetailsById(UUID id);

    @Override
    @EntityGraph(attributePaths = {"user"})
    Page<DeveloperProfile> findAll(Pageable pageable);
}
