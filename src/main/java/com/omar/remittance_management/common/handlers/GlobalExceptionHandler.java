package com.omar.remittance_management.common.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.omar.remittance_management.common.dto.ApiError;
import com.omar.remittance_management.common.exeption.BusinessException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleBusinessException(BusinessException ex) {
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            ex.getMessage(),
            ex.getCode()
        );
       
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleMethodArgumentNotValidException(org.springframework.web.bind.MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .reduce((msg1, msg2) -> msg1 + "; " + msg2)
            .orElse("Validation failed");
        return new ApiError(
            HttpStatus.BAD_REQUEST.value(),
            errorMessage,
            "VALIDATION_ERROR"
        );
    }
}
