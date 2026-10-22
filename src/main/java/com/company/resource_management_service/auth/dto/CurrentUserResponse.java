package com.company.resource_management_service.auth.dto;

import java.util.Set;
import java.util.UUID;

public record CurrentUserResponse(
        UUID userId,
        String email,
        String fullName,
        Set<String> roles,
        Set<String> permissions) {
}
