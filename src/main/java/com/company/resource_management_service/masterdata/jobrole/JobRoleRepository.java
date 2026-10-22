package com.company.resource_management_service.masterdata.jobrole;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRoleRepository extends JpaRepository<JobRole, UUID> {
    boolean existsByCode(String code);
}
