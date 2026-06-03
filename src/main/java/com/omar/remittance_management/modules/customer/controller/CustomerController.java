package com.omar.remittance_management.modules.customer.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.customer.dto.AddressDto;
import com.omar.remittance_management.modules.customer.dto.CustomerDto;
import com.omar.remittance_management.modules.customer.dto.SaveAddressRequest;
import com.omar.remittance_management.modules.customer.dto.SaveCustomer;
import com.omar.remittance_management.modules.customer.service.AddressService;
import com.omar.remittance_management.modules.customer.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AddressService addressService;

    // Create a new Customer
    @PostMapping
    public CustomerDto SaveCustomer(@RequestBody @Valid SaveCustomer customer) {
        return customerService.SaveCustomer(customer);

    }

    // Get all Customers
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();

    }

    // Get a Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Long id) {
        Optional<CustomerDto> customerDto = customerService.getCustomerById(id);
        return customerDto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a Customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable Long id, @RequestBody @Valid SaveCustomer request) {
        CustomerDto updatedCustomerDto = customerService.updateCustomer(id, request);
        return ResponseEntity.ok(updatedCustomerDto);
    }

    // Delete a Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        boolean deleted = customerService.deleteCustomer(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    // Add address to a customer
    @PostMapping("/{customerId}/addresses")
    public ResponseEntity<AddressDto> addAddressToCustomer(
            @PathVariable Long customerId,
            @RequestBody @Valid SaveAddressRequest request) {
        AddressDto addressDto = addressService.createAddress(customerId, request);
        return ResponseEntity.ok(addressDto);
    }

    // Edit customer address
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> editCustomerAddress(
            @PathVariable Long addressId,
            @RequestBody @Valid SaveAddressRequest request) {
        AddressDto updatedAddress = addressService.editCustomerAddress(addressId, request);
        return ResponseEntity.ok(updatedAddress);
    }

    // Delete customer address
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteCustomerAddress(@PathVariable Long addressId) {
        addressService.deleteCustomerAddress(addressId);

        return ResponseEntity.noContent().build();
    }

    // Get customer addresses
    @GetMapping("/{customerId}/addresses")
    public ResponseEntity<List<AddressDto>> getCustomerAddresses(@PathVariable Long customerId) {
        List<AddressDto> addresses = addressService.getCustomerAddresses(customerId);
        return ResponseEntity.ok(addresses);
    }

    // Get address by id
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDto> getAddressById(@PathVariable Long addressId) {
        AddressDto addressDto = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDto);
    }

}