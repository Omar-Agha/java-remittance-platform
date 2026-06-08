package com.omar.remittance_management.modules.feeRule.dto;

import java.math.BigDecimal;

public record SaveFeeRuleLineDto(BigDecimal amount, BigDecimal percentage) {

}
