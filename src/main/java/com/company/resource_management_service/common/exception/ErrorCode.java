package com.company.resource_management_service.common.exception;

import org.springframework.http.HttpStatus;

/** Central catalogue of business error codes mapped to HTTP status. */
public enum ErrorCode {

    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Resource not found"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),
    BUSINESS_RULE_VIOLATION(HttpStatus.UNPROCESSABLE_ENTITY, "Business rule violated"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "Resource already exists"),
    CONCURRENT_UPDATE(HttpStatus.CONFLICT, "Resource was modified by another transaction"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error");

    private final HttpStatus status;
    private final String defaultMessage;

    ErrorCode(HttpStatus status, String defaultMessage) {
        this.status = status;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus status() {
        return status;
    }

    public String defaultMessage() {
        return defaultMessage;
    }
}
