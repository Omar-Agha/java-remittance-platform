package com.omar.remittance_management.modules.currency.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SaveCurrencyDto(
        @NotBlank(message = "Currency code is required") @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters") @Pattern(regexp = "^[A-Z]{3}$", message = "Currency code must be 3 uppercase letters") String code,

        @NotBlank(message = "Currency name is required") String name,

        @Size(max = 5, message = "Symbol must be at most 5 characters") String symbol,

        // Default value handled typically in service/entity, optional for creation DTOs
        Boolean active) {
}
