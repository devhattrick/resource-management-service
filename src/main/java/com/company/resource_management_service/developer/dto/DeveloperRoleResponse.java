package com.company.resource_management_service.developer.dto;

import java.util.UUID;

public record DeveloperRoleResponse(
        UUID jobRoleId,
        String jobRoleCode,
        String jobRoleName,
        boolean primary) {
}
