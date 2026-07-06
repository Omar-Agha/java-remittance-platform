package com.omar.remittance_management.modules.feeCalculator.manager;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.omar.remittance_management.modules.feeCalculator.dto.PricingItemDto;
import com.omar.remittance_management.modules.feeCalculator.dto.PricingSummaryDto;
import com.omar.remittance_management.modules.feeCalculator.enums.PricingItemType;
import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.repository.FeeRuleRepo;
import com.omar.remittance_management.modules.tax.entity.Tax;
import com.omar.remittance_management.modules.tax.repository.TaxRepo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FeeCalculator {

    private final TaxRepo taxRepo;
    private final FeeRuleRepo feeRuleRepo;

    public PricingSummaryDto calculatePricingSummery(BigDecimal amount) {
        var taxes = taxRepo.findAllByActiveTrue();
        var activeFeeRule = feeRuleRepo
                .findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(Instant.now());
        List<FeeRule> activeFeeRules = activeFeeRule.map(List::of).orElseGet(List::of);

        var taxesResult = calculateTaxes(amount, taxes);
        var feesResult = calculateFees(amount, activeFeeRules);

        // Merge the items from both results for the PricingSummaryDto
        List<PricingItemDto> allItems = new java.util.ArrayList<>();
        allItems.addAll(feesResult.getItems());
        allItems.addAll(taxesResult.getItems());

        return new PricingSummaryDto(
                feesResult.getResult(),
                taxesResult.getResult(),
                allItems);

    }

    private FeeCalculationResult calculateTaxes(BigDecimal amount, List<Tax> taxes) {
        BigDecimal taxResult = BigDecimal.ZERO;
        List<PricingItemDto> items = new ArrayList<>();
        for (Tax tax : taxes) {
            var taxValue = tax.getRate().multiply(amount);
            taxResult = taxResult.add(taxValue);
            items.add(new PricingItemDto(taxValue, PricingItemType.Tax));
        }
        return FeeCalculationResult.builder()
                .result(taxResult)
                .items(items)
                .build();

    }

    private FeeCalculationResult calculateFees(BigDecimal amount, List<FeeRule> feeRules) {
        BigDecimal taxResult = BigDecimal.ZERO;
        List<PricingItemDto> items = new ArrayList<>();
        for (FeeRule rule : feeRules) {
            var feeValue = rule.calculateFee(amount);
            taxResult = taxResult.add(feeValue);
            items.add(new PricingItemDto(feeValue, PricingItemType.Fee));

        }
        return FeeCalculationResult.builder()
                .result(taxResult)
                .items(items)
                .build();

    }

}
