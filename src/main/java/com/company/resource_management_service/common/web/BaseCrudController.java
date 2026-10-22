package com.company.resource_management_service.common.web;

import com.company.resource_management_service.common.service.BaseCrudService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

/**
 * Reusable REST surface for simple CRUD resources. Concrete controllers add
 * {@code @RestController} + {@code @RequestMapping} and expose the service.
 * Reads require only authentication; writes require MASTER_MANAGE.
 */
public abstract class BaseCrudController<REQ, RES> {

    protected abstract BaseCrudService<?, REQ, RES> service();

    @GetMapping("/{id}")
    public ApiResponse<RES> getById(@PathVariable UUID id) {
        return ApiResponse.ok(service().getById(id));
    }

    @GetMapping
    public ApiResponse<PageResponse<RES>> list(Pageable pageable) {
        return ApiResponse.ok(service().list(pageable));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MASTER_MANAGE')")
    public ApiResponse<RES> create(@Valid @RequestBody REQ request) {
        return ApiResponse.ok(service().create(request), "Created");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MASTER_MANAGE')")
    public ApiResponse<RES> update(@PathVariable UUID id, @Valid @RequestBody REQ request) {
        return ApiResponse.ok(service().update(id, request), "Updated");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MASTER_MANAGE')")
    public ApiResponse<Void> delete(@PathVariable UUID id) {
        service().delete(id);
        return ApiResponse.ok(null, "Deleted");
    }
}
