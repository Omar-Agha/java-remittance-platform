package com.omar.remittance_management.common.exeption;

/**
 * Custom exception for business logic errors in the Remittance Management system.
 * Typically thrown when a rule or invariant in the business domain is violated.
 */
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}