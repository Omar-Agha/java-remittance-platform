package com.omar.remittance_management.modules.feeCalculator.dto;

import java.math.BigDecimal;

public record CalculateFeesRequestDto(
        BigDecimal amount) {

}
