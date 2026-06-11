package com.omar.remittance_management.modules.currency.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.omar.remittance_management.modules.currency.dto.CurrencyDto;
import com.omar.remittance_management.modules.currency.dto.SaveCurrencyDto;
import com.omar.remittance_management.modules.currency.entity.Currency;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface CurrencyMapper {

    CurrencyDto toDto(Currency currency);

    @Mapping(target = "id", ignore = true)
    Currency createDtoToEntity(SaveCurrencyDto dto);

    @Mapping(target = "id", ignore = true)
    void updateToEntity(SaveCurrencyDto dto, @MappingTarget Currency entity);

}
