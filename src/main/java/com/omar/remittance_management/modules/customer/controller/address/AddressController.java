package com.omar.remittance_management.modules.customer.controller.address;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.customer.dto.address.AddressDto;
import com.omar.remittance_management.modules.customer.dto.address.SaveAddressRequest;
import com.omar.remittance_management.modules.customer.service.address.AddressService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@RequestBody @Valid SaveAddressRequest request) {
        AddressDto addressDto = addressService.createAddress(request);
        return ResponseEntity.ok(addressDto);
    }

    // READ - get single address by id
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId) {
        AddressDto addressDto = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDto);
    }

    // READ - get all addresses for a customer
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AddressDto>> getAllAddressesByCustomerId(@PathVariable Long customerId) {
        List<AddressDto> addresses = addressService.getAllAddressesByCustomerId(customerId);
        return ResponseEntity.ok(addresses);
    }

    // UPDATE
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long addressId,
            @RequestBody @Valid SaveAddressRequest request) {
        AddressDto updated = addressService.updateAddress(addressId, request);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.noContent().build();
    }
}
