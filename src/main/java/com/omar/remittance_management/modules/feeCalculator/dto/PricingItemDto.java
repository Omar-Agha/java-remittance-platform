package com.omar.remittance_management.modules.feeCalculator.dto;

import java.math.BigDecimal;

import com.omar.remittance_management.modules.feeCalculator.enums.PricingItemType;

public record PricingItemDto(
        BigDecimal value,
        PricingItemType type) {
}