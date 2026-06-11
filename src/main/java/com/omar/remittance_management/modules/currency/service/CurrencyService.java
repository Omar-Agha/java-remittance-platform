package com.omar.remittance_management.modules.currency.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.remittance_management.common.exception.BusinessException;
import com.omar.remittance_management.modules.currency.dto.CurrencyDto;
import com.omar.remittance_management.modules.currency.dto.SaveCurrencyDto;
import com.omar.remittance_management.modules.currency.entity.Currency;
import com.omar.remittance_management.modules.currency.exception.CurrencyExceptionConts;
import com.omar.remittance_management.modules.currency.mapper.CurrencyMapper;
import com.omar.remittance_management.modules.currency.repository.CurrencyRepo;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CurrencyService {

    private final CurrencyRepo currencyRepo;
    private final CurrencyMapper currencyMapper;

    @Transactional(readOnly = true)
    public List<CurrencyDto> findAll() {
        return currencyRepo.findAll().stream()
                .map(currencyMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CurrencyDto findById(Long id) {
        Currency currency = currencyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));
        return currencyMapper.toDto(currency);
    }

    @Transactional(readOnly = true)
    public CurrencyDto findByCode(String code) {
        Currency currency = currencyRepo.findByCode(code)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with code: " + code));
        return currencyMapper.toDto(currency);
    }

    @Transactional
    public CurrencyDto create(SaveCurrencyDto dto) {
        // Business rules & validation handled here

        // Enforce code is uppercase
        String code = dto.code().toUpperCase();

        if (currencyRepo.existsByCode(code)) {
            throw new BusinessException(CurrencyExceptionConts.CURRENCY_CODE_ALREADY_EXISTS);
        }

        if (currencyRepo.existsByName(dto.name())) {
            throw new BusinessException(CurrencyExceptionConts.CURRENCY_NAME_ALREADY_EXISTS);
        }

        boolean active = dto.active() != null ? dto.active() : true;

        Currency currency = new Currency(
                code,
                dto.name(),
                dto.symbol(),
                active);

        try {
            currency = currencyRepo.save(currency);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(
                    "Failed to save Currency due to database constraint: " + ex.getMessage());
        }

        return currencyMapper.toDto(currency);
    }

    @Transactional
    public CurrencyDto update(Long id, SaveCurrencyDto dto) {
        Currency currency = currencyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));

        String code = dto.code().toUpperCase();

        if (currencyRepo.existsByCodeAndIdNot(code, id)) {
            throw new BusinessException(CurrencyExceptionConts.CURRENCY_CODE_ALREADY_EXISTS);
        }

        if (currencyRepo.existsByNameAndIdNot(dto.name(), id)) {
            throw new BusinessException(CurrencyExceptionConts.CURRENCY_NAME_ALREADY_EXISTS);
        }

        currency.setCode(code);
        currency.setName(dto.name());
        currency.setSymbol(dto.symbol());
        if (dto.active() != null) {
            currency.setActive(dto.active());
        }

        try {
            currency = currencyRepo.save(currency);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException(
                    "Failed to update Currency due to database constraint: " + ex.getMessage());
        }

        return currencyMapper.toDto(currency);
    }

    @Transactional
    public void delete(Long id) {
        Currency currency = currencyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));
        currencyRepo.delete(currency);
    }

    @Transactional
    public CurrencyDto setActive(Long id, boolean active) {
        Currency currency = currencyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));
        currency.setActive(active);
        currency = currencyRepo.save(currency);
        return currencyMapper.toDto(currency);
    }

}
