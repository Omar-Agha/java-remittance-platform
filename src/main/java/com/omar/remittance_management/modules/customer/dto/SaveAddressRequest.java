package com.omar.remittance_management.modules.customer.dto;

public record SaveAddressRequest(
        @jakarta.validation.constraints.NotBlank(message = "Address1 is required") String address1,

        String address2,

        String description,

        @jakarta.validation.constraints.Min(value = 0, message = "sort_order must not be negative") int sort_order) {

}
