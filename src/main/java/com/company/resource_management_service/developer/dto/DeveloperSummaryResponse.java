package com.company.resource_management_service.developer.dto;

import com.company.resource_management_service.developer.ProfileStatus;
import com.company.resource_management_service.developer.Seniority;

import java.util.UUID;

public record DeveloperSummaryResponse(
        UUID id,
        UUID userId,
        String employeeCode,
        String fullName,
        Seniority seniority,
        int capacityPercent,
        ProfileStatus profileStatus) {
}
