package com.omar.remittance_management.modules.feeRule.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omar.remittance_management.common.exception.BusinessException;
import com.omar.remittance_management.common.exception.EntityNotFoundException;
import com.omar.remittance_management.modules.feeRule.dto.ApproveFeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.FeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.SaveFeeRuleDto;
import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.entity.FeeRuleLine;
import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;
import com.omar.remittance_management.modules.feeRule.exception.ExceptionCodes;
import com.omar.remittance_management.modules.feeRule.mapper.FeeRuleMapper;
import com.omar.remittance_management.modules.feeRule.repository.FeeRuleRepo;

@Service
public class FeeRuleCrudService {

    @Autowired
    private FeeRuleRepo feeRuleRepo;
    @Autowired
    private FeeRuleMapper feeRuleMapper;

    @Transactional
    public FeeRuleDto createFeeRule(SaveFeeRuleDto saveFeeRuleDto) {
        if (saveFeeRuleDto.ruleName() == null || saveFeeRuleDto.ruleName().isBlank()) {
            throw new BusinessException("Rule name must not be null or blank");
        }
        if (feeRuleRepo.existsByRuleName(saveFeeRuleDto.ruleName())) {
            throw new BusinessException("Rule name must be unique");
        }

        if (saveFeeRuleDto.feeRuleType() == null) {
            throw new BusinessException(ExceptionCodes.INVALID_FEE_RULE_TYPE_WITH_LINES);
        }
        FeeRule feeRule = new FeeRule(
                saveFeeRuleDto.ruleName(),
                saveFeeRuleDto.feeRuleType());
        feeRule.setEffectiveDate(saveFeeRuleDto.effectiveDate(), Instant.now());

        List<FeeRuleLine> lines = (saveFeeRuleDto.lines() != null && !saveFeeRuleDto.lines().isEmpty())
                ? saveFeeRuleDto.lines().stream()
                        .map(dto -> new FeeRuleLine(
                                null, // id set to null for new entity
                                dto.amount(),
                                dto.percentage()))
                        .toList()
                : null;
        if (saveFeeRuleDto.feeRuleType() != FeeRuleType.Constant && lines != null && !lines.isEmpty()) {
            feeRule.addLines(lines);
        }
        FeeRule savedFeeRule = feeRuleRepo.save(feeRule);
        return feeRuleMapper.toDto(savedFeeRule);
    }

    @Transactional(readOnly = true)
    public List<FeeRuleDto> getAllFeeRules() {
        List<FeeRule> feeRules = feeRuleRepo.findAll();
        return feeRules.stream()
                .map(feeRuleMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<FeeRuleDto> getFeeRuleById(Long id) {
        return feeRuleRepo.findById(id).map(feeRuleMapper::toDto);
    }

    @Transactional
    public FeeRuleDto updateFeeRule(Long id, SaveFeeRuleDto saveFeeRuleDto) {
        FeeRule feeRule = feeRuleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FeeRule", id));

        // Check if the rule name is unique (exclude the current entity by id)
        if (saveFeeRuleDto.ruleName() != null && !saveFeeRuleDto.ruleName().isBlank()) {
            if (feeRuleRepo.existsByRuleNameAndIdNot(saveFeeRuleDto.ruleName(), id)) {
                throw new BusinessException("Rule name must be unique");
            }
        }
        if (feeRule.isApproved()) {
            throw new BusinessException("Cannot update an approved FeeRule.");
        }

        if (saveFeeRuleDto.ruleName() != null && !saveFeeRuleDto.ruleName().isBlank()) {
            feeRule.setRuleName(saveFeeRuleDto.ruleName());
        }

        if (saveFeeRuleDto.effectiveDate() != null) {
            feeRule.setEffectiveDate(saveFeeRuleDto.effectiveDate(), Instant.now());
        }

        if (saveFeeRuleDto.feeRuleType() != null) {
            feeRule.setFeeRuleType(saveFeeRuleDto.feeRuleType());
        }

        FeeRule updated = feeRuleRepo.save(feeRule);
        if (saveFeeRuleDto.lines() != null) {
            List<FeeRuleLine> lines = saveFeeRuleDto.lines().stream()
                    .map(dto -> new FeeRuleLine(
                            null, // id set to null for new entity
                            dto.amount(),
                            dto.percentage()))
                    .toList();
            feeRule.addLines(lines);
        }
        return feeRuleMapper.toDto(updated);
    }

    @Transactional
    public void deleteFeeRule(Long id) {
        if (!feeRuleRepo.existsById(id)) {
            throw new EntityNotFoundException("FeeRule", id);
        }
        feeRuleRepo.deleteById(id);
    }

    @Transactional
    public FeeRuleDto approveFeeRule(Long id, ApproveFeeRuleDto request) {
        FeeRule feeRule = feeRuleRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FeeRule", id));

        feeRule.approve(1L, "dummy user", Instant.now());
        FeeRule saved = feeRuleRepo.save(feeRule);
        return feeRuleMapper.toDto(saved);
    }

    public Optional<FeeRuleDto> getEffectiveFeeRule() {
        return feeRuleRepo
                .findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(Instant.now())
                .map(feeRuleMapper::toDto);

    }

}
