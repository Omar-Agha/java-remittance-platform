package com.omar.remittance_management.modules.remittanceIssue.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record IssRemDto(
        Long id,
        Long customerId,
        String customerPhone,
        String customerFullName,
        BigDecimal amount,
        String currencyCode,
        Long currencyId,
        Instant issueDate,
        Boolean isApproved,
        Instant approvedAt,
        Boolean isCashed,
        Instant cashedAt,
        String beneficiaryName,
        String beneficiaryPhone) {

}
