package com.company.resource_management_service.changerequest;

import com.company.resource_management_service.changerequest.dto.ChangeRequestResponse;
import com.company.resource_management_service.changerequest.dto.ChangeRequestSummaryResponse;
import com.company.resource_management_service.changerequest.dto.CreateChangeRequestRequest;
import com.company.resource_management_service.changerequest.dto.ReviewRequest;
import com.company.resource_management_service.common.web.ApiResponse;
import com.company.resource_management_service.common.web.PageResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/change-requests")
public class ChangeRequestController {

    private final ChangeRequestService service;

    public ChangeRequestController(ChangeRequestService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<PageResponse<ChangeRequestSummaryResponse>> list(Pageable pageable) {
        return ApiResponse.ok(service.list(pageable));
    }

    @GetMapping("/{id}")
    public ApiResponse<ChangeRequestResponse> getById(@PathVariable UUID id) {
        return ApiResponse.ok(service.getById(id));
    }

    @PostMapping
    public ApiResponse<ChangeRequestResponse> create(@Valid @RequestBody CreateChangeRequestRequest request) {
        return ApiResponse.ok(service.create(request), "Created");
    }

    @PostMapping("/{id}/submit")
    public ApiResponse<ChangeRequestResponse> submit(@PathVariable UUID id) {
        return ApiResponse.ok(service.submit(id), "Submitted");
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<ChangeRequestResponse> cancel(@PathVariable UUID id) {
        return ApiResponse.ok(service.cancel(id), "Cancelled");
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('SKILL_APPROVE')")
    public ApiResponse<ChangeRequestResponse> approve(
            @PathVariable UUID id, @RequestBody(required = false) ReviewRequest review) {
        return ApiResponse.ok(service.approve(id, review == null ? null : review.comment()), "Approved");
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('SKILL_APPROVE')")
    public ApiResponse<ChangeRequestResponse> reject(
            @PathVariable UUID id, @RequestBody(required = false) ReviewRequest review) {
        return ApiResponse.ok(service.reject(id, review == null ? null : review.comment()), "Rejected");
    }
}
