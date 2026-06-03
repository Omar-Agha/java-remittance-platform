package com.omar.remittance_management.modules.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.omar.remittance_management.common.exception.BusinessException;
import com.omar.remittance_management.modules.customer.dto.CustomerDto;
import com.omar.remittance_management.modules.customer.dto.SaveCustomer;
import com.omar.remittance_management.modules.customer.entity.Customer;
import com.omar.remittance_management.modules.customer.mapper.CustomerMapper;
import com.omar.remittance_management.modules.customer.repository.CustomerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;

    private final CustomerMapper customerMapper;

    // Create
    public CustomerDto SaveCustomer(SaveCustomer SaveCustomerDto) {
        // Check for unique national number
        if (customerRepo.existsByNationalNumber(SaveCustomerDto.nationalNumber())) {
            // throw new BusinessException("CUSTOMER_ALREADY_EXISTS", "National number
            // already exists", "Customer already exists");
            throw new BusinessException("CUSTOMER_ALREADY_EXISTS", "National number already exists");
        }
        // Check for unique email if provided

        if (customerRepo.existsByEmail(SaveCustomerDto.email())) {
            throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists");
        }
        Customer savedCustomer = customerRepo.save(customerMapper.createDtoToEntity(SaveCustomerDto));
        return customerMapper.toDto(savedCustomer);

    }

    // Read All
    public List<CustomerDto> getAllCustomers() {
        return customerRepo.findAll().stream()
                .map(customerMapper::toDto)
                .toList();
    }

    // Read by ID
    public Optional<CustomerDto> getCustomerById(Long id) {
        return customerRepo.findById(id).map(customerMapper::toDto);
    }

    // Update
    public CustomerDto updateCustomer(Long id, SaveCustomer request) {
        return customerRepo.findById(id).map(existing -> {
            // Check for unique national number (exclude current customer)

            if (customerRepo.existsByNationalNumberAndIdNot(request.nationalNumber(), id)) {
                throw new BusinessException("CUSTOMER_ALREADY_EXISTS", "National number already exists");
            }

            // Check for unique email (exclude current customer)

            if (customerRepo.existsByEmailAndIdNot(request.email(), id)) {
                throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists");
            }
            customerMapper.updateToEntity(request, existing);
            Customer savedCustomer = customerRepo.save(existing);
            return customerMapper.toDto(savedCustomer);

        }).orElseThrow(() -> new BusinessException("CUSTOMER_NOT_FOUND", "Customer not found"));

    }

    // Delete
    public boolean deleteCustomer(Long id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }

    // TODO: add method : attach array of addresses to a customer
    // TODO: add method : delete address for a customer
    // TODO: add method : update address for a customer

}