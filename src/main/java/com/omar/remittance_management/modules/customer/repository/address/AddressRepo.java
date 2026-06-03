package com.omar.remittance_management.modules.customer.repository.address;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omar.remittance_management.modules.customer.entity.address.Address;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {
    List<Address> findAllByCustomerId(Long customerId);
}
