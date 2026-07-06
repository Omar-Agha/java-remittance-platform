package com.omar.remittance_management.modules.feeRule.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.omar.remittance_management.common.exception.BusinessException;
import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;
import com.omar.remittance_management.modules.feeRule.exception.ExceptionCodes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fee_rule")
@NoArgsConstructor
@Getter
public class FeeRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @FutureOrPresent
    private Instant effectiveDate;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    private String ruleName;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeRuleType feeRuleType;

    @OneToMany(mappedBy = "feeRule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FeeRuleLine> lines = new ArrayList<>();

    @jakarta.validation.constraints.DecimalMin(value = "0.0", inclusive = true, message = "Constant rate must be at least 0.0")
    @jakarta.validation.constraints.DecimalMax(value = "1.0", inclusive = true, message = "Constant rate cannot be greater than 1.0")
    private BigDecimal constantRate;

    private boolean isApproved;
    @Column(nullable = true)
    private Long approvedByUserId;

    @Column(nullable = true)
    private String approvedByUserName;

    public void approve(Long userId, String userName, Instant now) {
        validateEffectiveDate(this.effectiveDate, now);
        this.isApproved = true;
        this.approvedByUserId = userId;
        this.approvedByUserName = userName;
    }

    private void validateEffectiveDate(Instant effectiveDate, Instant now) {
        if (effectiveDate == null || effectiveDate.isBefore(now)) {

            throw new BusinessException(ExceptionCodes.EFFECTIVE_DATE_IN_PAST);
        }
    }

    public FeeRule(String ruleName, FeeRuleType feeRuleType, BigDecimal constantRate) {

        this.ruleName = ruleName;
        this.feeRuleType = feeRuleType;
        this.constantRate = constantRate;
    }

    public void addLines(List<FeeRuleLine> newLines) {
        if (FeeRuleType.Constant.equals(this.feeRuleType)) {
            throw new BusinessException(ExceptionCodes.INVALID_FEE_RULE_TYPE_WITH_LINES);
        }

        if (this.lines == null) {
            this.lines = new java.util.ArrayList<>();
        }
        if (newLines != null) {
            for (FeeRuleLine line : newLines) {
                line.setFeeRule(this);
            }

            this.lines.addAll(newLines);
        }
    }

    public void setEffectiveDate(Instant effectiveDate, Instant now) {
        validateEffectiveDate(effectiveDate, now);
        this.effectiveDate = effectiveDate;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setConstantRate(BigDecimal constantRate) {
        this.constantRate = constantRate;
    }

    public void setFeeRuleType(FeeRuleType feeRuleType) {
        if (feeRuleType == FeeRuleType.Constant) {
            this.lines = new java.util.ArrayList<>();

        }
        this.feeRuleType = feeRuleType;
    }

    public BigDecimal calculateFee(BigDecimal amount) {
        return switch (this.feeRuleType) {
            case Constant -> calculateConstantRateValue(amount);
            case Range -> calculateDynamicRateValue(amount);
            default -> throw new BusinessException("Unsupported FeeRuleType: " + this.feeRuleType);

        };
    }

    public BigDecimal calculateConstantRateValue(BigDecimal amount) {
        if (this.feeRuleType != FeeRuleType.Constant) {
            throw new BusinessException("FeeRule type is not Constant (Constant)");
        }
        if (amount == null || this.constantRate == null) {
            return null;
        }
        return this.constantRate.multiply(amount);
    }

    /**
     * Calculates the dynamic (range-based) fee value for a given amount by using
     * the valid FeeRuleLine from this FeeRule's lines.
     * 
     * @param amount the transaction amount to calculate the fee for
     * @return the calculated fee as BigDecimal, or null if no valid line or input
     *         is found
     */
    public BigDecimal calculateDynamicRateValue(BigDecimal amount) {
        if (this.feeRuleType != FeeRuleType.Range) {
            throw new BusinessException("FeeRule type is not dynamic (Range)");
        }

        if (amount == null || lines == null || lines.isEmpty() || this.feeRuleType != FeeRuleType.Range) {
            return BigDecimal.ZERO;
        }
        FeeRuleLine validLine = getValidFeeRuleLine(amount);
        if (validLine == null || validLine.getPercentage() == null) {
            return BigDecimal.ZERO;
        }
        return validLine.getPercentage().multiply(amount);
    }

    public FeeRuleLine getValidFeeRuleLine(BigDecimal targetAmount) {

        if (lines == null || targetAmount == null)
            return null;
        return lines.stream()
                .filter(line -> line.getAmount() != null && line.getAmount().compareTo(targetAmount) <= 0)
                .max(java.util.Comparator.comparing(FeeRuleLine::getAmount))
                .orElse(null);
    }
}
