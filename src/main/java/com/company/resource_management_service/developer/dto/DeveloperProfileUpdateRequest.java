package com.company.resource_management_service.developer.dto;

import com.company.resource_management_service.developer.ProfileStatus;
import com.company.resource_management_service.developer.Seniority;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DeveloperProfileUpdateRequest(
        Seniority seniority,
        @NotNull @PositiveOrZero BigDecimal totalExperienceYears,
        @NotNull @Min(0) @Max(100) Integer capacityPercent,
        @NotNull ProfileStatus profileStatus,
        @Size(max = 2000) String note) {
}
