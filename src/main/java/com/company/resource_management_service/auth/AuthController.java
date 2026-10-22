package com.company.resource_management_service.auth;

import com.company.resource_management_service.auth.dto.CurrentUserResponse;
import com.company.resource_management_service.auth.dto.LoginRequest;
import com.company.resource_management_service.auth.dto.LoginResponse;
import com.company.resource_management_service.common.web.ApiResponse;
import com.company.resource_management_service.security.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ApiResponse<CurrentUserResponse> me() {
        return ApiResponse.ok(authService.getCurrentUser(SecurityUtils.currentUserId()));
    }
}
