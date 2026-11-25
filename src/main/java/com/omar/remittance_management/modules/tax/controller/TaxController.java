package com.omar.remittance_management.modules.tax.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.tax.dto.SaveTaxDto;
import com.omar.remittance_management.modules.tax.dto.TaxDto;
import com.omar.remittance_management.modules.tax.service.TaxService;

@RestController
@RequestMapping("/api/taxes")
public class TaxController {

    private final TaxService taxService;

    public TaxController(TaxService taxService) {
        this.taxService = taxService;
    }

    /**
     * Get all taxes.
     */
    @GetMapping
    public List<TaxDto> getAllTaxes() {
        return taxService.findAll();
    }

    /**
     * Get all active taxes.
     */
    @GetMapping("/active")
    public List<TaxDto> getAllActiveTaxes() {
        return taxService.findAllActive();
    }

    /**
     * Get a tax by id.
     */
    @GetMapping("/{id}")
    public TaxDto getTaxById(@PathVariable("id") Long id) {
        return taxService.findById(id);
    }

    /**
     * Get a tax by name.
     */
    @GetMapping("/by-name/{name}")
    public TaxDto getTaxByName(@PathVariable String name) {
        return taxService.findByName(name);
    }

    /**
     * Create a new tax.
     */
    @PostMapping
    public ResponseEntity<TaxDto> createTax(@RequestBody SaveTaxDto taxDto) {
        TaxDto created = taxService.create(taxDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    /**
     * Update an existing tax.
     */
    @PutMapping("/{id}")
    public TaxDto updateTax(@PathVariable Long id, @RequestBody SaveTaxDto taxDto) {
        return taxService.update(id, taxDto);
    }

    /**
     * Delete a tax by id.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTax(@PathVariable Long id) {
        taxService.delete(id);
    }
}
