package com.omar.remittance_management.modules.feeRule.entity;

import java.time.Instant;
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
    private List<FeeRuleLine> lines;

    private boolean isApproved;
    @Column(nullable = true)
    private Long approvedByUserId;

    @Column(nullable = true)
    private String approvedByUserName;

    public void approve(Long userId, String userName) {
        validateEffectiveDate(this.effectiveDate);
        this.isApproved = true;
        this.approvedByUserId = userId;
        this.approvedByUserName = userName;
    }

    private void validateEffectiveDate(Instant effectiveDate) {
        if (effectiveDate == null || effectiveDate.isBefore(Instant.now())) {

            throw new BusinessException(ExceptionCodes.EFFECTIVE_DATE_IN_PAST);
        }
    }

    public FeeRule(String ruleName, FeeRuleType feeRuleType, Instant effectiveDate) {
        validateEffectiveDate(effectiveDate);
        this.ruleName = ruleName;
        this.feeRuleType = feeRuleType;
        this.effectiveDate = effectiveDate;
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

    public void setEffectiveDate(Instant effectiveDate) {
        validateEffectiveDate(effectiveDate);
        this.effectiveDate = effectiveDate;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setFeeRuleType(FeeRuleType feeRuleType) {
        if (feeRuleType == FeeRuleType.Constant) {
            this.lines = new java.util.ArrayList<>();

        }
        this.feeRuleType = feeRuleType;
    }

}
