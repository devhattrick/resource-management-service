package com.company.resource_management_service.developer.dto;

import com.company.resource_management_service.developer.ProfileStatus;
import com.company.resource_management_service.developer.Seniority;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record DeveloperProfileResponse(
        UUID id,
        UUID userId,
        String employeeCode,
        String email,
        String fullName,
        Seniority seniority,
        BigDecimal totalExperienceYears,
        int capacityPercent,
        ProfileStatus profileStatus,
        String note,
        List<DeveloperRoleResponse> roles,
        List<DeveloperLanguageResponse> languages) {
}
