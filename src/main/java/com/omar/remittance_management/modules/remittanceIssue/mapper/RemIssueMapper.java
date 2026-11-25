package com.omar.remittance_management.modules.remittanceIssue.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.omar.remittance_management.modules.remittanceIssue.dto.IssRemDto;
import com.omar.remittance_management.modules.remittanceIssue.entity.IssRem;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RemIssueMapper {

    IssRemDto toDto(IssRem issRem);

    IssRem toEntity(IssRemDto issRemDto);

}
