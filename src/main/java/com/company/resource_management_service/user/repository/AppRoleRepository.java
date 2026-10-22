package com.company.resource_management_service.user.repository;

import com.company.resource_management_service.user.entity.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppRoleRepository extends JpaRepository<AppRole, UUID> {

    Optional<AppRole> findByCode(String code);
}
