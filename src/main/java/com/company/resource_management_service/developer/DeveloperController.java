package com.company.resource_management_service.developer;

import com.company.resource_management_service.common.web.ApiResponse;
import com.company.resource_management_service.common.web.PageResponse;
import com.company.resource_management_service.developer.dto.DeveloperProfileCreateRequest;
import com.company.resource_management_service.developer.dto.DeveloperProfileResponse;
import com.company.resource_management_service.developer.dto.DeveloperProfileUpdateRequest;
import com.company.resource_management_service.developer.dto.DeveloperSummaryResponse;
import com.company.resource_management_service.developer.dto.ReplaceLanguagesRequest;
import com.company.resource_management_service.developer.dto.ReplaceRolesRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperProfileService service;

    public DeveloperController(DeveloperProfileService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResponse<DeveloperSummaryResponse>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<DeveloperProfileResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PROFILE_MANAGE')")
    public ApiResponse<DeveloperProfileResponse> create(@Valid @RequestBody DeveloperProfileCreateRequest request) {
        return ApiResponse.ok(service.create(request), "Created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('PROFILE_EDIT_OWN','PROFILE_MANAGE')")
    public ApiResponse<DeveloperProfileResponse> updateCore(
            @PathVariable UUID id, @Valid @RequestBody DeveloperProfileUpdateRequest request) {
        return ApiResponse.ok(service.updateCore(id, request), "Updated");
    }

    @PutMapping("/{id}/roles")
    @PreAuthorize("hasAnyAuthority('PROFILE_EDIT_OWN','PROFILE_MANAGE')")
    public ApiResponse<DeveloperProfileResponse> replaceRoles(
            @PathVariable UUID id, @Valid @RequestBody ReplaceRolesRequest request) {
        return ApiResponse.ok(service.replaceRoles(id, request.roles()), "Roles updated");
    }

    @PutMapping("/{id}/languages")
    @PreAuthorize("hasAnyAuthority('PROFILE_EDIT_OWN','PROFILE_MANAGE')")
    public ApiResponse<DeveloperProfileResponse> replaceLanguages(
            @PathVariable UUID id, @Valid @RequestBody ReplaceLanguagesRequest request) {
        return ApiResponse.ok(service.replaceLanguages(id, request.languages()), "Languages updated");
    }
}
