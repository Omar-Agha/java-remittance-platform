package com.omar.remittance_management.modules.tax.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omar.remittance_management.modules.tax.entity.Tax;

public interface TaxRepo extends JpaRepository<Tax, Long> {
    Optional<Tax> findByName(String name);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    java.util.List<Tax> findAllByActiveTrue();
}
