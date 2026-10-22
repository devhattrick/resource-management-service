package com.company.resource_management_service.masterdata.category;

import java.util.UUID;

public record SkillCategoryResponse(
        UUID id,
        String code,
        String name,
        int sortOrder,
        boolean active) {
}
