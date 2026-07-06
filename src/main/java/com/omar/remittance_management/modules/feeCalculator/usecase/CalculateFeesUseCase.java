package com.omar.remittance_management.modules.feeCalculator.usecase;

import org.springframework.stereotype.Component;

import com.omar.remittance_management.modules.feeCalculator.dto.CalculateFeesRequestDto;
import com.omar.remittance_management.modules.feeCalculator.dto.PricingSummaryDto;
import com.omar.remittance_management.modules.feeCalculator.manager.FeeCalculator;

@Component
public class CalculateFeesUseCase {

    private final FeeCalculator feeCalculator;

    public CalculateFeesUseCase(FeeCalculator feeCalculator) {
        this.feeCalculator = feeCalculator;
    }

    public PricingSummaryDto execute(CalculateFeesRequestDto request) {
        return feeCalculator.calculatePricingSummery(request.amount());
    }

}
