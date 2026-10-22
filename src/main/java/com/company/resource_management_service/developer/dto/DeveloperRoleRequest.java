package com.company.resource_management_service.developer.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DeveloperRoleRequest(
        @NotNull UUID jobRoleId,
        Boolean primary) {
}
