package com.omar.remittance_management.modules.remittanceIssue.service;

import java.math.BigDecimal;
import java.util.Currency;

import org.springframework.stereotype.Service;

import com.omar.remittance_management.common.exception.EntityNotFoundException;
import com.omar.remittance_management.modules.currency.repository.CurrencyRepo;
import com.omar.remittance_management.modules.customer.entity.Customer;
import com.omar.remittance_management.modules.customer.repository.CustomerRepo;
import com.omar.remittance_management.modules.remittanceIssue.dto.CreateOrUpdateIssRemDto;
import com.omar.remittance_management.modules.remittanceIssue.dto.IssRemDto;
import com.omar.remittance_management.modules.remittanceIssue.entity.IssRem;
import com.omar.remittance_management.modules.remittanceIssue.managers.RemIssueManager;
import com.omar.remittance_management.modules.remittanceIssue.mapper.RemIssueMapper;
import com.omar.remittance_management.modules.remittanceIssue.repository.IssRemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUpdateRemUseCase {

        private final CurrencyRepo currencyRepository;
        private final CustomerRepo customerRepository;
        private final RemIssueMapper remMapper;
        private final IssRemRepository issRemRepository;
        private final RemIssueManager remManager;

        public IssRemDto execute(Long id, CreateOrUpdateIssRemDto request) {
                // Fetch currency by id and ensure it exists
                var currency = currencyRepository.findById(request.currencyId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                Currency.class, request.currencyId()));
                Long currencyId = currency.getId();
                String currencyCode = currency.getCode();

                // Fetch customer by id and ensure it exists
                var customer = customerRepository.findById(request.customerId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                Customer.class, request.customerId()));
                Long customerId = customer.getId();
                String customerPhone = customer.getPhoneNumber();
                String customerFullName = customer.getFullName();

                // Get beneficiary data from DTO
                String beneficiaryName = request.beneficiaryName();
                String beneficiaryPhone = request.beneficiaryPhone();

                // Get amount from request
                BigDecimal amount = request.amount();

                // Insert or update entity in database
                boolean isUpdating = (id != null);
                IssRem issRem = remManager.createOrUpdate(
                                isUpdating,
                                id,
                                customerId,
                                customerPhone,
                                customerFullName,
                                amount,
                                currencyCode,
                                currencyId,
                                beneficiaryName,
                                beneficiaryPhone);

                // Construct and return IssRemDto
                return remMapper.toDto(issRem);
        }

}
