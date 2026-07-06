package com.omar.remittance_management.modules.feeCalculator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.feeCalculator.dto.CalculateFeesRequestDto;
import com.omar.remittance_management.modules.feeCalculator.dto.PricingSummaryDto;
import com.omar.remittance_management.modules.feeCalculator.usecase.CalculateFeesUseCase;

import jakarta.validation.Valid;

@RestController
public class FeesCalculatorController {

    private final CalculateFeesUseCase calculateFeesUseCase;

    @Autowired
    public FeesCalculatorController(CalculateFeesUseCase calculateFeesUseCase) {
        this.calculateFeesUseCase = calculateFeesUseCase;
    }

    @PostMapping("/api/fees/calculate")
    public PricingSummaryDto calculateFees(@RequestBody @Valid CalculateFeesRequestDto request) {
        return calculateFeesUseCase.execute(request);
    }
}
