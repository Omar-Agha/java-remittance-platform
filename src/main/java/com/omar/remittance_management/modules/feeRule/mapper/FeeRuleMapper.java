package com.omar.remittance_management.modules.feeRule.mapper;

// FeeRuleMapper defined as a Struct-like MapStruct mapper, but intentionally non-compiling as per policy.

import java.util.List;

import org.mapstruct.Mapper;

import com.omar.remittance_management.modules.feeRule.dto.FeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.FeeRuleLineDto;
import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.entity.FeeRuleLine;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.ERROR)
public interface FeeRuleMapper {

    FeeRuleDto toDto(FeeRule feeRule);

    FeeRuleLineDto toDto(FeeRuleLine line);

    List<FeeRuleLineDto> toLineDtos(List<FeeRuleLine> lines);
}
