package com.omar.remittance_management.modules.feeRule.dto;

import java.time.Instant;
import java.util.List;

import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;

public record SaveFeeRuleDto(
                @jakarta.validation.constraints.Future Instant effectiveDate,

                String ruleName,
                @jakarta.validation.constraints.NotNull(message = "FeeRuleType is required") FeeRuleType feeRuleType,

                List<SaveFeeRuleLineDto> lines) {
}
