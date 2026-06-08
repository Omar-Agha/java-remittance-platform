package com.omar.remittance_management.modules.feeRule.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fee_rule_lines")
@NoArgsConstructor

@Getter
@Setter
public class FeeRuleLine {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @DecimalMin(value = "0.0", inclusive = false, message = "Percentage must be greater than 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "Percentage cannot be greater than 1")
    private BigDecimal percentage;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fee_rule_id", nullable = false)
    private FeeRule feeRule;

    public FeeRuleLine(Long id, @Positive(message = "Amount must be greater than 0") BigDecimal amount,
            @DecimalMin(value = "0.0", inclusive = false, message = "Percentage must be greater than 0") @DecimalMax(value = "1.0", inclusive = true, message = "Percentage cannot be greater than 1") BigDecimal percentage) {
        this.id = id;
        this.amount = amount;
        this.percentage = percentage;
    }

}
