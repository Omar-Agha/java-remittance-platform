package com.omar.remittance_management.modules.feeCalculator.usecase;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.omar.remittance_management.modules.feeCalculator.dto.CalculateFeesRequestDto;
import com.omar.remittance_management.modules.feeCalculator.dto.PricingSummaryDto;
import com.omar.remittance_management.modules.feeCalculator.manager.FeeCalculator;
import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.repository.FeeRuleRepo;
import com.omar.remittance_management.modules.tax.entity.Tax;
import com.omar.remittance_management.modules.tax.repository.TaxRepo;

@ExtendWith(MockitoExtension.class)
public class FeeCalculatorTest {

    @Mock
    private TaxRepo taxRepo;

    @Mock
    private FeeRuleRepo feeRuleRepo;

    private FeeCalculator feeCalculator;

    private CalculateFeesUseCase calculateFeesUseCase;

    private final BigDecimal AMOUNT = new BigDecimal("1234.56");

    @BeforeEach
    void setUp() {
        feeCalculator = new FeeCalculator(taxRepo, feeRuleRepo);
        calculateFeesUseCase = new CalculateFeesUseCase(feeCalculator);
    }

    @Test
    void execute_ReturnsPricingSummaryDto_WithFiveDummyTaxes() {
        // Given
        // Let's create 5 dummy taxes with a known rate: 1%, 2%, 3%, 4%, 5%
        var taxes = java.util.List.of(
                dummyTax(new BigDecimal("0.01")),
                dummyTax(new BigDecimal("0.02")),
                dummyTax(new BigDecimal("0.03")),
                dummyTax(new BigDecimal("0.04")),
                dummyTax(new BigDecimal("0.05")));

        // Calculate expected total tax: sum of each (rate * amount)
        BigDecimal expectedTaxTotal = taxes.stream()
                .map(tax -> tax.getRate().multiply(AMOUNT))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Let's dummy a fee total as zero, since this test is about taxes
        BigDecimal expectedFeeTotal = BigDecimal.ZERO;

        PricingSummaryDto expectedSummary = new PricingSummaryDto(
                expectedTaxTotal,
                expectedFeeTotal,
                java.util.Collections.emptyList());

        CalculateFeesRequestDto request = new CalculateFeesRequestDto(AMOUNT);

        // Mock repositories: taxRepo returns our dummy taxes; feeRuleRepo returns empty
        when(taxRepo.findAllByActiveTrue()).thenReturn(taxes);
        when(feeRuleRepo.findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                org.mockito.ArgumentMatchers.any()))
                .thenReturn(java.util.Optional.empty());

        // When
        PricingSummaryDto result = calculateFeesUseCase.execute(request);

        // Then
        assertThat(result.totalTaxes()).isEqualByComparingTo(expectedTaxTotal);
        assertThat(result.totalFees()).isEqualByComparingTo(expectedFeeTotal);
        // Optionally, test that there were 5 dummy taxes used, if summary keeps items:
        assertThat(result.pricingItems()).hasSize(5);
    }

    @Test
    void execute_ReturnsPricingSummaryDto_WithTenDummyTaxesAndFeeRule() {
        // Given
        // 10 dummy taxes: 0.01 (1%) to 0.10 (10%) increments
        var taxes = java.util.List.of(
                dummyTax(new BigDecimal("0.01")),
                dummyTax(new BigDecimal("0.02")),
                dummyTax(new BigDecimal("0.03")),
                dummyTax(new BigDecimal("0.04")),
                dummyTax(new BigDecimal("0.05")),
                dummyTax(new BigDecimal("0.06")),
                dummyTax(new BigDecimal("0.07")),
                dummyTax(new BigDecimal("0.08")),
                dummyTax(new BigDecimal("0.09")),
                dummyTax(new BigDecimal("0.10")));

        // 1 dummy fee rule with known behavior: always returns 123.45 as fee for any
        // amount
        FeeRule dummyFeeRule = org.mockito.Mockito.mock(FeeRule.class);
        org.mockito.Mockito.when(dummyFeeRule.calculateFee(AMOUNT)).thenReturn(new BigDecimal("123.45"));

        var feeRules = java.util.List.of(dummyFeeRule);

        // Calculate expected sums
        BigDecimal expectedTaxTotal = taxes.stream()
                .map(tax -> tax.getRate().multiply(AMOUNT))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expectedFeeTotal = new BigDecimal("123.45");

        CalculateFeesRequestDto request = new CalculateFeesRequestDto(AMOUNT);

        // Mock repo returns
        when(taxRepo.findAllByActiveTrue()).thenReturn(taxes);
        // Mock feeRuleRepo returns our one dummy fee rule in an Optional
        when(feeRuleRepo.findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                org.mockito.ArgumentMatchers.any()))
                .thenReturn(java.util.Optional.of(dummyFeeRule));

        // When
        PricingSummaryDto result = calculateFeesUseCase.execute(request);

        // Then
        assertThat(result.totalTaxes()).isEqualByComparingTo(expectedTaxTotal);
        assertThat(result.totalFees()).isEqualByComparingTo(expectedFeeTotal);

        // There should be 10 tax items and 1 fee item
        assertThat(result.pricingItems()).hasSize(11);
        // Optionally check types
        long taxCount = result.pricingItems().stream()
                .filter(item -> item
                        .type() == com.omar.remittance_management.modules.feeCalculator.enums.PricingItemType.Tax)
                .count();
        long feeCount = result.pricingItems().stream()
                .filter(item -> item
                        .type() == com.omar.remittance_management.modules.feeCalculator.enums.PricingItemType.Fee)
                .count();
        assertThat(taxCount).isEqualTo(10);
        assertThat(feeCount).isEqualTo(1);
    }

    // Helper method to create a dummy Tax object with a specified rate
    private static Tax dummyTax(BigDecimal rate) {
        Tax tax = new Tax();
        tax.setRate(rate);
        return tax;
    }
}
