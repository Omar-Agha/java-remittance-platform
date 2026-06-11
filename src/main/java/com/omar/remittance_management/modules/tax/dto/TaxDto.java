package com.omar.remittance_management.modules.tax.dto;

import java.math.BigDecimal;

public record TaxDto(
        Long id,
        String name,
        BigDecimal rate,
        Boolean active) {
}
