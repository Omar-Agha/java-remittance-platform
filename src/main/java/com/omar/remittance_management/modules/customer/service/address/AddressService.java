package com.omar.remittance_management.modules.customer.service.address;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.remittance_management.common.exception.EntityNotFoundException;
import com.omar.remittance_management.modules.customer.dto.address.AddressDto;
import com.omar.remittance_management.modules.customer.dto.address.SaveAddressRequest;
import com.omar.remittance_management.modules.customer.entity.Customer;
import com.omar.remittance_management.modules.customer.entity.address.Address;
import com.omar.remittance_management.modules.customer.mapper.address.AddressMapper;
import com.omar.remittance_management.modules.customer.repository.CustomerRepo;
import com.omar.remittance_management.modules.customer.repository.address.AddressRepo;

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
    public AddressDto createAddress(SaveAddressRequest request) {
        Customer customer = customerRepo.findById(request.customerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", request.customerId()));
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
    public List<AddressDto> getAllAddressesByCustomerId(Long customerId) {
        List<Address> addresses = addressRepo.findAllByCustomerId(customerId);
        return addresses.stream().map(addressMapper::toDto).toList();
    }

    // UPDATE
    @Transactional
    public AddressDto updateAddress(Long addressId, SaveAddressRequest request) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address", addressId));

        // Optionally, re-associate customer
        if (!address.getCustomer().getId().equals(request.customerId())) {
            Customer customer = customerRepo.findById(request.customerId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Customer", request.customerId()));
            address.setCustomer(customer);
        }
        addressMapper.updateToEntity(request, address);
        Address updated = addressRepo.save(address);
        return addressMapper.toDto(updated);
    }

    // DELETE
    @Transactional
    public void deleteAddress(Long addressId) {
        if (!addressRepo.existsById(addressId)) {
            throw new EntityNotFoundException("Address", addressId);
        }
        addressRepo.deleteById(addressId);
    }
}
