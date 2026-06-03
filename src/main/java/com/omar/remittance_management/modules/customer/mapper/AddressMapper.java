package com.omar.remittance_management.modules.customer.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.omar.remittance_management.modules.customer.dto.AddressDto;
import com.omar.remittance_management.modules.customer.dto.SaveAddressRequest;
import com.omar.remittance_management.modules.customer.entity.Address;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface AddressMapper {

    // @Mapping(target = "customer", ignore = true)
    AddressDto toDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    Address createDtoToEntity(SaveAddressRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    void updateToEntity(SaveAddressRequest request, @MappingTarget Address entity);
}
