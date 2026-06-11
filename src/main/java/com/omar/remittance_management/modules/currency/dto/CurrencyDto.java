package com.omar.remittance_management.modules.currency.dto;

public record CurrencyDto(
        Long id,
        String code,
        String name,
        String symbol,
        boolean active) {

}