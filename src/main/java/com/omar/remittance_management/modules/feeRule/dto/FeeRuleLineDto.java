package com.omar.remittance_management.modules.feeRule.dto;

import java.math.BigDecimal;

public record FeeRuleLineDto(Long id, BigDecimal amount, BigDecimal percentage) {
}
