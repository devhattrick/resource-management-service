package com.company.resource_management_service.auth;

import com.company.resource_management_service.auth.dto.CurrentUserResponse;
import com.company.resource_management_service.auth.dto.LoginRequest;
import com.company.resource_management_service.auth.dto.LoginResponse;
import com.company.resource_management_service.common.exception.BusinessException;
import com.company.resource_management_service.common.exception.ErrorCode;
import com.company.resource_management_service.security.TokenProvider;
import com.company.resource_management_service.user.entity.AppPermission;
import com.company.resource_management_service.user.entity.AppRole;
import com.company.resource_management_service.user.entity.AppUser;
import com.company.resource_management_service.user.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(AppUserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        AppUser user = userRepository.findByEmailIgnoreCase(request.email())
                .filter(AppUser::isActive)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }

        Set<String> permissions = collectPermissions(user);
        String token = tokenProvider.createAccessToken(user.getId(), user.getEmail(), permissions);

        return new LoginResponse(
                token,
                "Bearer",
                tokenProvider.getValiditySeconds(),
                user.getId(),
                user.getFullName(),
                permissions);
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(UUID userId) {
        AppUser user = userRepository.findWithRolesById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        Set<String> roles = user.getRoles().stream()
                .map(AppRole::getCode)
                .collect(Collectors.toSet());

        return new CurrentUserResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                roles,
                collectPermissions(user));
    }

    private Set<String> collectPermissions(AppUser user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(AppPermission::getCode)
                .collect(Collectors.toSet());
    }
}
