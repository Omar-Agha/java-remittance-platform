package com.omar.remittance_management.modules.remittanceIssue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.omar.remittance_management.modules.remittanceIssue.entity.IssRem;

public interface IssRemRepository extends JpaRepository<IssRem, Long> {

}
