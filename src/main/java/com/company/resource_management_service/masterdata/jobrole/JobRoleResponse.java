package com.company.resource_management_service.masterdata.jobrole;

import java.util.UUID;

public record JobRoleResponse(
        UUID id,
        String code,
        String name,
        String description,
        boolean active) {
}
