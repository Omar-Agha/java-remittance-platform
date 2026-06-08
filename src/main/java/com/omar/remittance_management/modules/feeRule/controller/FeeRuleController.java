package com.omar.remittance_management.modules.feeRule.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.feeRule.dto.ApproveFeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.FeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.SaveFeeRuleDto;
import com.omar.remittance_management.modules.feeRule.service.FeeRuleCrudService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fee-rules")
public class FeeRuleController {

    private final FeeRuleCrudService feeRuleCrudService;

    public FeeRuleController(FeeRuleCrudService feeRuleCrudService) {
        this.feeRuleCrudService = feeRuleCrudService;
    }

    // Create FeeRule
    @PostMapping
    public ResponseEntity<FeeRuleDto> createFeeRule(@RequestBody @Valid SaveFeeRuleDto saveFeeRuleDto) {
        FeeRuleDto feeRule = feeRuleCrudService.createFeeRule(saveFeeRuleDto);
        return ResponseEntity.ok(feeRule);
    }

    // Get all FeeRules
    @GetMapping
    public ResponseEntity<List<FeeRuleDto>> getAllFeeRules() {
        List<FeeRuleDto> feeRules = feeRuleCrudService.getAllFeeRules();

        return ResponseEntity.ok(feeRules);
    }

    // Get FeeRule by ID
    @GetMapping("/{id}")
    public ResponseEntity<FeeRuleDto> getFeeRuleById(@PathVariable Long id) {
        Optional<FeeRuleDto> feeRuleOpt = feeRuleCrudService.getFeeRuleById(id);
        return feeRuleOpt.map(feeRule -> ResponseEntity.ok(feeRule))
                .orElse(ResponseEntity.notFound().build());
    }

    // Update FeeRule
    @PutMapping("/{id}")
    public ResponseEntity<FeeRuleDto> updateFeeRule(
            @PathVariable Long id,
            @RequestBody @Valid SaveFeeRuleDto saveFeeRuleDto) {
        FeeRuleDto updated = feeRuleCrudService.updateFeeRule(id, saveFeeRuleDto);
        return ResponseEntity.ok(updated);
    }

    // Delete FeeRule
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeeRule(@PathVariable Long id) {
        feeRuleCrudService.deleteFeeRule(id);
        return ResponseEntity.noContent().build();
    }

    // Approve FeeRule
    @PostMapping("/{id}/approve")
    public ResponseEntity<FeeRuleDto> approveFeeRule(
            @PathVariable Long id, @RequestBody @Valid ApproveFeeRuleDto request) {
        FeeRuleDto feeRule = feeRuleCrudService.approveFeeRule(id, request);
        return ResponseEntity.ok(feeRule);
    }

    // Return the effective FeeRule for given parameters (example: sourceCountry,
    // destinationCountry, corridor, etc.)
    @GetMapping("/effective")
    public ResponseEntity<FeeRuleDto> getEffectiveFeeRule() {
        Optional<FeeRuleDto> effectiveFeeRule = feeRuleCrudService.getEffectiveFeeRule();
        return effectiveFeeRule
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
