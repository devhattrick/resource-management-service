package com.company.resource_management_service.developer.dto;

import com.company.resource_management_service.developer.Seniority;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record DeveloperProfileCreateRequest(
        @NotNull UUID userId,
        Seniority seniority,
        @PositiveOrZero BigDecimal totalExperienceYears,
        @Min(0) @Max(100) Integer capacityPercent,
        @Size(max = 2000) String note) {
}
