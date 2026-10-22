package com.company.resource_management_service.masterdata.skill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    boolean existsByCode(String code);
}
