package com.omar.remittance_management.modules.customer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omar.remittance_management.modules.customer.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    Optional<Customer> findByNationalNumber(String nationalNumber);
    Optional<Customer> findByEmail(String email);
}