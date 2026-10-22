package com.company.resource_management_service.changerequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ChangeRequestRepository extends JpaRepository<ChangeRequest, UUID> {

    @EntityGraph(attributePaths = {"items", "developer", "developer.user", "requestedBy", "reviewedBy"})
    Optional<ChangeRequest> findWithItemsById(UUID id);

    @EntityGraph(attributePaths = {"developer", "developer.user", "requestedBy"})
    Page<ChangeRequest> findByRequestedById(UUID requestedById, Pageable pageable);

    @Override
    @EntityGraph(attributePaths = {"developer", "developer.user", "requestedBy"})
    Page<ChangeRequest> findAll(Pageable pageable);
}
