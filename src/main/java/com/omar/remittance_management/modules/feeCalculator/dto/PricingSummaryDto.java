package com.omar.remittance_management.modules.feeCalculator.dto;

import java.math.BigDecimal;
import java.util.List;

public record PricingSummaryDto(
                BigDecimal totalFees,
                BigDecimal totalTaxes,
                List<PricingItemDto> pricingItems) {

}
