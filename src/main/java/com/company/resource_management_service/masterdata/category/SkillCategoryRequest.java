package com.company.resource_management_service.masterdata.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record SkillCategoryRequest(
        @NotBlank @Size(max = 30) String code,
        @NotBlank @Size(max = 100) String name,
        @PositiveOrZero Integer sortOrder,
        Boolean active) {
}
