package com.omar.remittance_management.modules.remittanceIssue.dto;

import java.math.BigDecimal;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record CreateOrUpdateIssRemDto(

        @NotNull(message = "Customer id is required") Long customerId,

        @NotNull(message = "Amount is required") @DecimalMin(value = "0.01", message = "Amount must be greater than 0") BigDecimal amount,

        @NotNull(message = "Currency id is required") Long currencyId,

        @NotNull(message = "Beneficiary name is required") String beneficiaryName,

        @Nullable String beneficiaryPhone) {
}
