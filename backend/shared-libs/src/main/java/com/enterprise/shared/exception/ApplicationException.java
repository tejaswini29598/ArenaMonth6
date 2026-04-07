package com.enterprise.shared.exception;

/**
 * Base exception class for all application exceptions
 */
public class ApplicationException extends RuntimeException {
    private int statusCode;

    public ApplicationException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public ApplicationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 500;
    }

    public int getStatusCode() {
        return statusCode;
    }
}

/**
 * Resource not found exception
 */
class ResourceNotFoundException extends ApplicationException {
    public ResourceNotFoundException(String message) {
        super(message, 404);
    }
}

/**
 * Validation exception
 */
class ValidationException extends ApplicationException {
    public ValidationException(String message) {
        super(message, 400);
    }
}

/**
 * Unauthorized exception
 */
class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String message) {
        super(message, 401);
    }
}

/**
 * Forbidden exception
 */
class ForbiddenException extends ApplicationException {
    public ForbiddenException(String message) {
        super(message, 403);
    }
}
