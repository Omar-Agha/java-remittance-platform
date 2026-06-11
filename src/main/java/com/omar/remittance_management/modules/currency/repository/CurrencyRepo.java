package com.omar.remittance_management.modules.currency.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omar.remittance_management.modules.currency.entity.Currency;

public interface CurrencyRepo extends JpaRepository<Currency, Long> {

    Optional<Currency> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByName(String name);

    // For update scenarios (existence by code/name excluding a specific id)
    boolean existsByCodeAndIdNot(String code, Long id);

    boolean existsByNameAndIdNot(String name, Long id);

}
