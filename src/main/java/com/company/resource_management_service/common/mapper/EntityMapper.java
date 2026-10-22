package com.company.resource_management_service.common.mapper;

/**
 * Contract for converting between an entity and its request/response DTOs.
 * Implemented per feature; lets BaseCrudService stay generic.
 */
public interface EntityMapper<E, REQ, RES> {

    E toEntity(REQ request);

    RES toResponse(E entity);

    /** Apply mutable fields from the request onto an existing managed entity. */
    void updateEntity(REQ request, E target);
}
