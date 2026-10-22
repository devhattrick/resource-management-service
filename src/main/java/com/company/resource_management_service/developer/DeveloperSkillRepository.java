package com.company.resource_management_service.developer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeveloperSkillRepository extends JpaRepository<DeveloperSkill, UUID> {

    boolean existsByDeveloperIdAndSkillId(UUID developerId, UUID skillId);

    List<DeveloperSkill> findByDeveloperId(UUID developerId);
}
