package com.omar.remittance_management.modules.remittanceIssue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.omar.remittance_management.modules.remittanceIssue.dto.CreateOrUpdateIssRemDto;
import com.omar.remittance_management.modules.remittanceIssue.dto.IssRemDto;
import com.omar.remittance_management.modules.remittanceIssue.service.CreateUpdateRemUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/remittance-issues")
public class RemIssueController {

    @Autowired
    private CreateUpdateRemUseCase createUpdateRemUseCase;

    // Create or update a remittance issue
    @PostMapping
    public ResponseEntity<IssRemDto> createOrUpdateRemittanceIssue(
            @RequestParam(value = "id", required = false) Long id,
            @Valid @RequestBody CreateOrUpdateIssRemDto requestDto) {
        IssRemDto result = createUpdateRemUseCase.execute(id, requestDto);
        return ResponseEntity.ok(result);
    }

}
