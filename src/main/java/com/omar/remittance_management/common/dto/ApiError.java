package com.omar.remittance_management.common.dto;

import java.time.LocalDateTime;

public record ApiError(int status, String message, String code, LocalDateTime timestamp) {
    public ApiError(int status, String message,String code) {
        this(status, message, code, LocalDateTime.now());
    }
}