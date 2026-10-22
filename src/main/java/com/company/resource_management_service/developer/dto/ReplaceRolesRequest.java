package com.company.resource_management_service.developer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ReplaceRolesRequest(
        @NotNull @Valid List<DeveloperRoleRequest> roles) {
}
