package com.company.resource_management_service.common.service;

import com.company.resource_management_service.common.entity.BaseEntity;
import com.company.resource_management_service.common.exception.ResourceNotFoundException;
import com.company.resource_management_service.common.mapper.EntityMapper;
import com.company.resource_management_service.common.web.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Reusable CRUD skeleton for simple master-data aggregates.
 * Feature services extend this and supply a repository + mapper;
 * only non-trivial business logic is overridden.
 */
public abstract class BaseCrudService<E extends BaseEntity, REQ, RES> {

    protected final JpaRepository<E, UUID> repository;
    protected final EntityMapper<E, REQ, RES> mapper;

    protected BaseCrudService(JpaRepository<E, UUID> repository, EntityMapper<E, REQ, RES> mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    /** Human-readable name used in not-found messages. */
    protected abstract String entityName();

    @Transactional(readOnly = true)
    public RES getById(UUID id) {
        return mapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public PageResponse<RES> list(Pageable pageable) {
        return PageResponse.from(repository.findAll(pageable).map(mapper::toResponse));
    }

    @Transactional
    public RES create(REQ request) {
        E entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public RES update(UUID id, REQ request) {
        E entity = findEntity(id);
        mapper.updateEntity(request, entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void delete(UUID id) {
        repository.delete(findEntity(id));
    }

    protected E findEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName(), id));
    }
}
