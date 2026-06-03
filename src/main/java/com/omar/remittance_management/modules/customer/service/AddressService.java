package com.omar.remittance_management.modules.customer.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.remittance_management.common.exception.EntityNotFoundException;
import com.omar.remittance_management.modules.customer.dto.AddressDto;
import com.omar.remittance_management.modules.customer.dto.SaveAddressRequest;
import com.omar.remittance_management.modules.customer.entity.Address;
import com.omar.remittance_management.modules.customer.entity.Customer;
import com.omar.remittance_management.modules.customer.mapper.AddressMapper;
import com.omar.remittance_management.modules.customer.repository.AddressRepo;
import com.omar.remittance_management.modules.customer.repository.CustomerRepo;

@Service
public class AddressService {

    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;
    private final AddressMapper addressMapper;

    public AddressService(AddressRepo addressRepo, CustomerRepo customerRepo, AddressMapper addressMapper) {
        this.addressRepo = addressRepo;
        this.customerRepo = customerRepo;
        this.addressMapper = addressMapper;
    }

    // CREATE
    @Transactional
    public AddressDto createAddress(Long customerId, SaveAddressRequest request) {
        Customer customer = customerRepo.findById(
                customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer", customerId));
        Address address = addressMapper.createDtoToEntity(request);
        address.setCustomer(customer);
        Address saved = addressRepo.save(address);
        return addressMapper.toDto(saved);
    }

    // READ - Get by ID
    public AddressDto getAddressById(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address", addressId));
        return addressMapper.toDto(address);
    }

    // READ - Get all by Customer ID
    public List<AddressDto> getCustomerAddresses(Long customerId) {
        List<Address> addresses = addressRepo.findAllByCustomerId(customerId);
        return addresses.stream().map(addressMapper::toDto).toList();
    }

    // UPDATE
    @Transactional
    public AddressDto editCustomerAddress(Long addressId, SaveAddressRequest request) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address", addressId));

        addressMapper.updateToEntity(request, address);
        Address updated = addressRepo.save(address);
        return addressMapper.toDto(updated);
    }

    // DELETE
    @Transactional
    public void deleteCustomerAddress(Long addressId) {
        if (!addressRepo.existsById(addressId)) {
            throw new EntityNotFoundException("Address", addressId);
        }
        addressRepo.deleteById(addressId);
    }
}
