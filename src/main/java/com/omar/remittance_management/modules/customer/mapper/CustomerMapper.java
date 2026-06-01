package com.omar.remittance_management.modules.customer.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.omar.remittance_management.modules.customer.dto.CustomerDto;
import com.omar.remittance_management.modules.customer.entity.Customer;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy=ReportingPolicy.ERROR
    
)
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);
}