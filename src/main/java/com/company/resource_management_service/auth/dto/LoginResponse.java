package com.company.resource_management_service.auth.dto;

import java.util.Set;
import java.util.UUID;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        String fullName,
        Set<String> permissions) {
}
