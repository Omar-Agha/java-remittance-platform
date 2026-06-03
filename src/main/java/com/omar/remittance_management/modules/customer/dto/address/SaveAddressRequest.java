package com.omar.remittance_management.modules.customer.dto.address;

public record SaveAddressRequest(
                String address1,
                String address2,
                String description,
                int sort_order,
                Long customerId) {
}
