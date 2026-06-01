package com.omar.remittance_management.modules.customer.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.omar.remittance_management.modules.customer.dto.CustomerDto;
import com.omar.remittance_management.modules.customer.dto.SaveCustomer;
import com.omar.remittance_management.modules.customer.entity.Customer;

@Mapper(componentModel = "spring",
    unmappedTargetPolicy=ReportingPolicy.ERROR
    
)
public interface CustomerMapper {

    CustomerDto toDto(Customer customer);

    Customer toEntity(CustomerDto customerDto);

    @Mapping(target = "id", ignore = true)
    Customer createDtoToEntity(SaveCustomer request);

    @Mapping(target = "id", ignore = true)
    void updateToEntity(SaveCustomer request, @MappingTarget Customer entity);
    
}
 