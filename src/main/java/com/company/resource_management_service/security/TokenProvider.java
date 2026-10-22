package com.company.resource_management_service.security;

import java.util.Collection;
import java.util.UUID;

/**
 * Abstraction over access-token issuance (Adapter/Port).
 * The rest of the app depends on this, not on a concrete JWT library,
 * so swapping to Cognito/Keycloak later touches only the implementation.
 */
public interface TokenProvider {

    String createAccessToken(UUID userId, String username, Collection<String> permissions);

    long getValiditySeconds();
}
