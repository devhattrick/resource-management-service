package com.company.resource_management_service.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/** Nimbus/Spring-Security implementation of {@link TokenProvider} (symmetric HS256). */
@Component
public class JwtTokenProvider implements TokenProvider {

    private static final String ISSUER = "resource-management";

    private final JwtEncoder jwtEncoder;
    private final long validitySeconds;

    public JwtTokenProvider(JwtEncoder jwtEncoder,
                            @Value("${app.security.jwt.validity-seconds:3600}") long validitySeconds) {
        this.jwtEncoder = jwtEncoder;
        this.validitySeconds = validitySeconds;
    }

    @Override
    public String createAccessToken(UUID userId, String username, Collection<String> permissions) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(ISSUER)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(validitySeconds))
                .subject(userId.toString())
                .claim("username", username)
                .claim("permissions", List.copyOf(permissions))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public long getValiditySeconds() {
        return validitySeconds;
    }
}
