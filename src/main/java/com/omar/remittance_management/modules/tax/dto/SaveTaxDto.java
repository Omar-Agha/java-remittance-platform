package com.omar.remittance_management.modules.tax.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SaveTaxDto(
        @NotBlank(message = "Tax name must not be blank") String name,

        @NotNull(message = "Tax rate is required") @DecimalMin(value = "0.00", inclusive = true, message = "Tax rate must be at least 0.00") @DecimalMax(value = "1.00", inclusive = true, message = "Tax rate must be at most 1.00") BigDecimal rate,

        @NotNull(message = "Active status must be specified") Boolean active) {
}
