package com.omar.remittance_management.modules.customer.dto;

public record AddressDto(
        Long id,
        String address1,
        String address2,
        String description,
        int sort_order,
        Long customerId) {
}
