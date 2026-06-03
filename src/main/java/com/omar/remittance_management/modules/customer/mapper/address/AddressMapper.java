package com.omar.remittance_management.modules.customer.mapper.address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.omar.remittance_management.modules.customer.entity.address.Address;
import com.omar.remittance_management.modules.customer.dto.address.AddressDto;
import com.omar.remittance_management.modules.customer.dto.address.SaveAddressRequest;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface AddressMapper {

    // @Mapping(target = "customer", ignore = true)
    AddressDto toDto(Address address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Address createDtoToEntity(SaveAddressRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateToEntity(SaveAddressRequest request, @MappingTarget Address entity);
}
