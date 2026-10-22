package com.company.resource_management_service.masterdata.category;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillCategoryRepository extends JpaRepository<SkillCategory, UUID> {
    boolean existsByCode(String code);
}
