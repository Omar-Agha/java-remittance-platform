package com.omar.remittance_management.modules.feeRule.dto;

import java.time.Instant;
import java.util.List;

import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;

public record FeeRuleDto(
                Long id,
                Instant effectiveDate,
                String ruleName,
                FeeRuleType feeRuleType,
                List<FeeRuleLineDto> lines) {
}
