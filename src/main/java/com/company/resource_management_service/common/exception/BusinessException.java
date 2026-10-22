package com.company.resource_management_service.common.exception;

/** Base class for all expected (handled) domain errors. */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode) {
        this(errorCode, errorCode.defaultMessage());
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
