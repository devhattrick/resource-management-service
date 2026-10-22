package com.company.resource_management_service.masterdata.skill;

import java.util.UUID;

public record SkillResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        String code,
        String name,
        String description,
        boolean active) {
}
