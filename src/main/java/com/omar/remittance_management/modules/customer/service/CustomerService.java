package com.omar.remittance_management.modules.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.omar.remittance_management.common.exeption.BusinessException;
import com.omar.remittance_management.modules.customer.dto.CreateCustomer;
import com.omar.remittance_management.modules.customer.entity.Customer;
import com.omar.remittance_management.modules.customer.repository.CustomerRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;

    // Create
    public Customer createCustomer(CreateCustomer createCustomerDto) {
        // Check for unique national number
        if (customerRepo.findByNationalNumber(createCustomerDto.nationalNumber()).isPresent()) {
            // throw new BusinessException("CUSTOMER_ALREADY_EXISTS", "National number already exists", "Customer already exists");
            throw new BusinessException("CUSTOMER_ALREADY_EXISTS", "National number already exists");
        }
        // Check for unique email if provided
        if (createCustomerDto.email() != null && !createCustomerDto.email().isBlank()) {
            if (customerRepo.findByEmail(createCustomerDto.email()).isPresent()) {
                throw new BusinessException("EMAIL_ALREADY_EXISTS", "Email already exists");
            }
        }
        Customer customer = Customer.builder()
            .nationalNumber(createCustomerDto.nationalNumber())
            .firstName(createCustomerDto.firstName())
            .lastName(createCustomerDto.lastName())
            .email(createCustomerDto.email())
            .phoneNumber(createCustomerDto.phoneNumber())
            .birthDate(createCustomerDto.birthDate())
            .address(createCustomerDto.address())
            .nationality(createCustomerDto.nationality())
            .isActive(createCustomerDto.isActive())
            .build();
        return customerRepo.save(customer);
    }

    // Read All
    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    // Read by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepo.findById(id);
    }

    // Read by national number
    public Optional<Customer> getCustomerByNationalNumber(String nationalNumber) {
        return customerRepo.findByNationalNumber(nationalNumber);
    }

    // Update
    public Optional<Customer> updateCustomer(Long id, Customer updatedCustomer) {
        return customerRepo.findById(id).map(existing -> {
            existing.setNationalNumber(updatedCustomer.getNationalNumber());
            existing.setFirstName(updatedCustomer.getFirstName());
            existing.setLastName(updatedCustomer.getLastName());
            existing.setEmail(updatedCustomer.getEmail());
            existing.setPhoneNumber(updatedCustomer.getPhoneNumber());
            existing.setBirthDate(updatedCustomer.getBirthDate());
            existing.setAddress(updatedCustomer.getAddress());
            existing.setNationality(updatedCustomer.getNationality());
            existing.setIsActive(updatedCustomer.getIsActive());
            return customerRepo.save(existing);
        });
    }

    // Delete
    public boolean deleteCustomer(Long id) {
        if (customerRepo.existsById(id)) {
            customerRepo.deleteById(id);
            return true;
        }
        return false;
    }
}