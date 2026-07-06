package com.omar.remittance_management.modules.feeRule.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.entity.FeeRuleLine;
import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;

public class FeeRuleTest {

    @Nested
    class ConstantRateCalculationTests {

        @Test
        void calculateConstantRateValueReturnsCorrectResult_whenConstantRateIsFractionBetween0And1() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, new BigDecimal("0.15")); // 15%
            BigDecimal amount = new BigDecimal("200"); // 15% of 200 = 30
            BigDecimal result = feeRule.calculateConstantRateValue(amount);
            assertThat(result).isEqualByComparingTo(new BigDecimal("30.00"));
        }

        @Test
        void calculateConstantRateValueReturnsZero_whenAmountIsZero() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, new BigDecimal("0.15"));
            BigDecimal amount = BigDecimal.ZERO;
            BigDecimal result = feeRule.calculateConstantRateValue(amount);
            assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        void calculateConstantRateValueReturnsNullIfAmountIsNull() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, new BigDecimal("0.50"));
            BigDecimal result = feeRule.calculateConstantRateValue(null);
            assertThat(result).isNull();
        }

        @Test
        void calculateConstantRateValueReturnsNullIfConstantRateIsNull() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, null);
            BigDecimal amount = new BigDecimal("100");
            BigDecimal result = feeRule.calculateConstantRateValue(amount);
            assertThat(result).isNull();
        }

        @Test
        void calculateConstantRateValueWorksWithMaxBoundaryAt1() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, BigDecimal.ONE); // 100%
            BigDecimal amount = new BigDecimal("150");
            BigDecimal result = feeRule.calculateConstantRateValue(amount);
            assertThat(result).isEqualByComparingTo(new BigDecimal("150.00"));
        }

        @Test
        void calculateConstantRateValueWorksWithMinBoundaryAt0() {
            FeeRule feeRule = new FeeRule("Constant Rule", FeeRuleType.Constant, BigDecimal.ZERO); // 0%
            BigDecimal amount = new BigDecimal("150");
            BigDecimal result = feeRule.calculateConstantRateValue(amount);
            assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    class DynamicRateCalculationTests {
        @Test
        void getValidFeeRuleLine_returnsMaxLineLessThanOrEqualToAmount() {
            FeeRule feeRule = new FeeRule("Range Rule", FeeRuleType.Range, BigDecimal.ZERO);

            FeeRuleLine line1 = new FeeRuleLine();
            line1.setAmount(new BigDecimal("100"));
            FeeRuleLine line2 = new FeeRuleLine();
            line2.setAmount(new BigDecimal("200"));
            FeeRuleLine line3 = new FeeRuleLine();
            line3.setAmount(new BigDecimal("300"));
            feeRule.addLines(Arrays.asList(line1, line2, line3));

            // Test when amount equals one of the line amounts
            FeeRuleLine valid = feeRule.getValidFeeRuleLine(new BigDecimal("200"));
            assertThat(valid).isNotNull();
            assertThat(valid.getAmount()).isEqualTo(new BigDecimal("200"));

            // Test for amount between two lines (should get max line <= amount)
            valid = feeRule.getValidFeeRuleLine(new BigDecimal("250"));
            assertThat(valid).isNotNull();
            assertThat(valid.getAmount()).isEqualTo(new BigDecimal("200"));

            // Test for amount less than all lines (should be null)
            valid = feeRule.getValidFeeRuleLine(new BigDecimal("50"));
            assertThat(valid).isNull();

            // Test for amount greater than all lines (should get max)
            valid = feeRule.getValidFeeRuleLine(new BigDecimal("1000"));
            assertThat(valid).isNotNull();
            assertThat(valid.getAmount()).isEqualTo(new BigDecimal("300"));

            // Test null lines or empty lines
            FeeRule emptyRule = new FeeRule("No Lines Rule", FeeRuleType.Range, BigDecimal.ZERO);
            assertThat(emptyRule.getValidFeeRuleLine(new BigDecimal("100"))).isNull();

            // Test null amount
            assertThat(feeRule.getValidFeeRuleLine(null)).isNull();
        }

        @Test
        void calculateDynamicRateValue_handlesAllCasesIncludingEdges() {
            FeeRule feeRule = new FeeRule("Range Rule", FeeRuleType.Range, BigDecimal.ZERO);

            FeeRuleLine line1 = new FeeRuleLine();
            line1.setAmount(new BigDecimal("100"));
            line1.setPercentage(new BigDecimal("0.10")); // 10%
            FeeRuleLine line2 = new FeeRuleLine();
            line2.setAmount(new BigDecimal("200"));
            line2.setPercentage(new BigDecimal("0.20")); // 20%
            FeeRuleLine line3 = new FeeRuleLine();
            line3.setAmount(new BigDecimal("300"));
            line3.setPercentage(new BigDecimal("0.30")); // 30%
            feeRule.addLines(Arrays.asList(line1, line2, line3));

            // Normal case: amount exactly matches a line amount
            BigDecimal fee = feeRule.calculateDynamicRateValue(new BigDecimal("200"));
            assertThat(fee).isEqualByComparingTo(new BigDecimal("40.00")); // 200*0.20

            // Case: amount between two line amounts, should pick line2 (max <= amount)
            fee = feeRule.calculateDynamicRateValue(new BigDecimal("250"));
            assertThat(fee).isEqualByComparingTo(new BigDecimal("50.00")); // 250*0.20

            // Case: amount equals largest line amount (should use line3)
            fee = feeRule.calculateDynamicRateValue(new BigDecimal("300"));
            assertThat(fee).isEqualByComparingTo(new BigDecimal("90.00")); // 300*0.30

            // Case: amount above highest line (should use largest line, line3)
            fee = feeRule.calculateDynamicRateValue(new BigDecimal("350"));
            assertThat(fee).isEqualByComparingTo(new BigDecimal("105.00")); // 350*0.30

            // Case: amount below all lines -- should return null
            fee = feeRule.calculateDynamicRateValue(new BigDecimal("50"));
            assertThat(fee).isEqualTo(BigDecimal.ZERO);

            // Edge: There are no lines in the rule (empty list)
            FeeRule noLinesRule = new FeeRule("No Lines", FeeRuleType.Range, BigDecimal.ZERO);
            fee = noLinesRule.calculateDynamicRateValue(new BigDecimal("150"));
            assertThat(fee).isEqualTo(BigDecimal.ZERO);

            // Edge: Null lines field
            FeeRule nullLinesRule = new FeeRule("Null Lines", FeeRuleType.Range, BigDecimal.ZERO);
            // Intentionally not adding any lines (keeps field at default null)
            fee = nullLinesRule.calculateDynamicRateValue(new BigDecimal("100"));
            assertThat(fee).isEqualTo(BigDecimal.ZERO);

            // Edge: Null amount argument
            fee = feeRule.calculateDynamicRateValue(null);
            assertThat(fee).isEqualTo(BigDecimal.ZERO);

            // Edge: Valid line, but percentage is null
            FeeRule nullPercentRule = new FeeRule("Null Percent", FeeRuleType.Range, BigDecimal.ZERO);
            FeeRuleLine lp = new FeeRuleLine();
            lp.setAmount(new BigDecimal("100"));
            lp.setPercentage(null);
            nullPercentRule.addLines(List.of(lp));
            fee = nullPercentRule.calculateDynamicRateValue(new BigDecimal("120"));
            assertThat(fee).isEqualTo(BigDecimal.ZERO);
        }
    }
}
