package com.company.resource_management_service.common.exception;

/** Thrown when an entity lookup by id/key fails. */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String entityName, Object id) {
        super(ErrorCode.RESOURCE_NOT_FOUND, "%s not found: %s".formatted(entityName, id));
    }
}
