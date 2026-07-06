package com.omar.remittance_management.modules.feeRule.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;

public record SaveFeeRuleDto(
        @jakarta.validation.constraints.Future Instant effectiveDate,

        String ruleName,
        @jakarta.validation.constraints.NotNull(message = "FeeRuleType is required") FeeRuleType feeRuleType,
        @jakarta.validation.constraints.DecimalMin(value = "0.0", inclusive = true, message = "Constant rate must be at least 0.0") @jakarta.validation.constraints.DecimalMax(value = "1.0", inclusive = true, message = "Constant rate cannot be greater than 1.0") BigDecimal constantRate,

        List<SaveFeeRuleLineDto> lines) {
}
