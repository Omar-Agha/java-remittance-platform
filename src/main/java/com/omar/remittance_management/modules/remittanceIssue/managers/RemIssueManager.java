package com.omar.remittance_management.modules.remittanceIssue.managers;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.omar.remittance_management.modules.remittanceIssue.entity.IssRem;
import com.omar.remittance_management.modules.remittanceIssue.repository.IssRemRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RemIssueManager {

    private final IssRemRepository issRemRepository;

    public IssRem createOrUpdate(
            boolean isUpdating,
            Long id,
            Long customerId,
            String customerPhone,
            String customerFullName,
            BigDecimal amount,
            String currencyCode,
            Long currencyId,
            String beneficiaryName,
            String beneficiaryPhone) {
        if (isUpdating) {
            return update(
                    id,
                    customerId,
                    customerPhone,
                    customerFullName,
                    amount,
                    currencyCode,
                    currencyId,
                    beneficiaryName,
                    beneficiaryPhone);
        } else {
            return create(
                    customerId,
                    customerPhone,
                    customerFullName,
                    amount,
                    currencyCode,
                    currencyId,
                    beneficiaryName,
                    beneficiaryPhone);
        }
    }

    public IssRem create(Long customerId, String customerPhone, String customerFullName,
            BigDecimal amount, String currencyCode, Long currencyId,
            String beneficiaryName, String beneficiaryPhone) {

        IssRem issRem = new IssRem(
                customerId,
                customerPhone,
                customerFullName,
                amount,
                currencyCode,
                currencyId,
                Instant.now(),
                beneficiaryName,
                beneficiaryPhone);

        return issRemRepository.save(issRem);
    }

    public IssRem update(Long id, Long customerId, String customerPhone, String customerFullName,
            BigDecimal amount, String currencyCode, Long currencyId,
            String beneficiaryName, String beneficiaryPhone) {

        IssRem existingIssRem = issRemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Remittance Issue not found with id " + id));

        existingIssRem.setCustomerId(customerId);
        existingIssRem.setCustomerPhone(customerPhone);
        existingIssRem.setCustomerFullName(customerFullName);
        existingIssRem.setAmount(amount);
        existingIssRem.setCurrencyCode(currencyCode);
        existingIssRem.setCurrencyId(currencyId);
        existingIssRem.setIssueDate(Instant.now());
        existingIssRem.setBeneficiaryName(beneficiaryName);
        existingIssRem.setBeneficiaryPhone(beneficiaryPhone);

        return issRemRepository.save(existingIssRem);
    }
}
