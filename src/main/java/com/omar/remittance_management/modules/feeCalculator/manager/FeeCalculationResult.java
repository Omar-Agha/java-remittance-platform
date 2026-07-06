package com.omar.remittance_management.modules.feeCalculator.manager;

import java.math.BigDecimal;
import java.util.List;

import com.omar.remittance_management.modules.feeCalculator.dto.PricingItemDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FeeCalculationResult {
    private BigDecimal result;
    private List<PricingItemDto> items;
}
