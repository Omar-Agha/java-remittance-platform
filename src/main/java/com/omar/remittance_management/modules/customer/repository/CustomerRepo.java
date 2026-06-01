package com.omar.remittance_management.modules.customer.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.omar.remittance_management.modules.customer.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
    boolean existsByNationalNumber(String nationalNumber);

    boolean existsByEmail(String email);

    boolean existsByNationalNumberAndIdNot(String nationalNumber, Long id);

    boolean existsByEmailAndIdNot(String email, Long id);
}