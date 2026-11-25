package com.omar.remittance_management.modules.currency.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.currency.dto.CurrencyDto;
import com.omar.remittance_management.modules.currency.dto.SaveCurrencyDto;
import com.omar.remittance_management.modules.currency.service.CurrencyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        
        return ResponseEntity.ok(currencyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrencyById(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.findById(id));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CurrencyDto> getCurrencyByCode(@PathVariable String code) {
        return ResponseEntity.ok(currencyService.findByCode(code));
    }

    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@Valid @RequestBody SaveCurrencyDto dto) {
        CurrencyDto created = currencyService.create(dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(@PathVariable Long id,
            @Valid @RequestBody SaveCurrencyDto dto) {
        CurrencyDto updated = currencyService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrency(@PathVariable Long id) {
        currencyService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CurrencyDto> activateCurrency(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.setActive(id, true));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CurrencyDto> deactivateCurrency(@PathVariable Long id) {
        return ResponseEntity.ok(currencyService.setActive(id, false));
    }
}
