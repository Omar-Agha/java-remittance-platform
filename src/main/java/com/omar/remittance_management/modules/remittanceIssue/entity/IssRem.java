package com.omar.remittance_management.modules.remittanceIssue.entity;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class IssRem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long customerId;

    @NotNull
    private String customerPhone;

    @NotNull
    private String customerFullName;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Nullable
    private String currencyCode;

    @NotNull
    private Long currencyId;

    @NotNull
    @FutureOrPresent(message = "Issue date must be in the present or future")
    private Instant issueDate;

    private Boolean isApproved = false;
    @Nullable
    private Instant approvedAt = null;
    private Boolean isCashed = false;
    @Nullable
    private Instant cashedAt = null;

    @NotNull
    private String beneficiaryName;
    @Nullable
    private String beneficiaryPhone;

    public IssRem(Long customerId, String customerPhone, String customerFullName,
            BigDecimal amount, String currencyCode, Long currencyId, Instant issueDate,
            String beneficiaryName, String beneficiaryPhone) {
        this.customerId = customerId;
        this.customerPhone = customerPhone;
        this.customerFullName = customerFullName;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.currencyId = currencyId;
        this.issueDate = issueDate;
        this.beneficiaryName = beneficiaryName;
        this.beneficiaryPhone = beneficiaryPhone;
    }

}
