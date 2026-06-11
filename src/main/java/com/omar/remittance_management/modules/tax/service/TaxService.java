package com.omar.remittance_management.modules.tax.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.remittance_management.modules.tax.dto.SaveTaxDto;
import com.omar.remittance_management.modules.tax.dto.TaxDto;
import com.omar.remittance_management.modules.tax.entity.Tax;
import com.omar.remittance_management.modules.tax.exception.TaxExceptionConsts;
import com.omar.remittance_management.modules.tax.mapper.TaxMapper;
import com.omar.remittance_management.modules.tax.repository.TaxRepo;

@Service
public class TaxService {

    private final TaxRepo taxRepo;
    private final TaxMapper taxMapper;

    @Autowired
    public TaxService(TaxRepo taxRepo, TaxMapper taxMapper) {
        this.taxRepo = taxRepo;
        this.taxMapper = taxMapper;
    }

    /**
     * Find all taxes.
     */
    public List<TaxDto> findAll() {
        List<Tax> all = taxRepo.findAll();
        return all.stream()
                .map(taxMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find all active taxes.
     */
    public List<TaxDto> findAllActive() {
        List<Tax> all = taxRepo.findAllByActiveTrue();
        return all.stream()
                .map(taxMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Find a tax by id.
     */
    public TaxDto findById(Long id) {
        Tax tax = taxRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tax not found"));
        return taxMapper.toDto(tax);
    }

    /**
     * Find a tax by name.
     */
    public TaxDto findByName(String name) {
        Tax tax = taxRepo.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Tax not found with name: " + name));
        return taxMapper.toDto(tax);
    }

    /**
     * Create a new tax.
     */
    @Transactional
    public TaxDto create(SaveTaxDto saveTaxDto) {
        // Unique name check
        if (taxRepo.existsByName(saveTaxDto.name())) {
            throw new IllegalArgumentException(TaxExceptionConsts.TAX_NAME_ALREADY_EXISTS);
        }
        Tax tax = taxMapper.toEntity(saveTaxDto);
        Tax saved = taxRepo.save(tax);
        return taxMapper.toDto(saved);
    }

    /**
     * Update an existing tax.
     */
    @Transactional
    public TaxDto update(Long id, SaveTaxDto saveTaxDto) {
        Tax existing = taxRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tax not found"));
        // Unique name check for update (exclude self)
        if (taxRepo.existsByNameAndIdNot(saveTaxDto.name(), id)) {
            throw new IllegalArgumentException(TaxExceptionConsts.TAX_NAME_ALREADY_EXISTS);
        }
        // Update fields
        existing.setName(saveTaxDto.name());
        existing.setRate(saveTaxDto.rate());
        existing.setActive(saveTaxDto.active());
        Tax updated = taxRepo.save(existing);
        return taxMapper.toDto(updated);
    }

    /**
     * Delete a tax by id.
     */
    @Transactional
    public void delete(Long id) {
        if (!taxRepo.existsById(id)) {
            throw new IllegalArgumentException("Tax not found");
        }
        taxRepo.deleteById(id);
    }

}
