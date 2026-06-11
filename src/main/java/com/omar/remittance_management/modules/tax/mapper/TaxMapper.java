package com.omar.remittance_management.modules.tax.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.omar.remittance_management.modules.tax.dto.SaveTaxDto;
import com.omar.remittance_management.modules.tax.dto.TaxDto;
import com.omar.remittance_management.modules.tax.entity.Tax;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TaxMapper {
    TaxDto toDto(Tax tax);

    Tax toEntity(TaxDto taxDto);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    Tax toEntity(SaveTaxDto taxDto);
}
