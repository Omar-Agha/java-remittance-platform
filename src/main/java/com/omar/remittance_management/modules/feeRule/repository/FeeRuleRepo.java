package com.omar.remittance_management.modules.feeRule.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.omar.remittance_management.modules.feeRule.entity.FeeRule;

@Repository
public interface FeeRuleRepo extends JpaRepository<FeeRule, Long> {

    @EntityGraph(attributePaths = "lines")
    Optional<FeeRule> findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(Instant now);

    // Optional<FeeRule> findFirstByIsApprovedTrueOrderByEffectiveDateDesc();
    // FeeRule findFirstByApprovedOrderByApprovalDateDesc(boolean approved);

    // For create: check if rule name already exists
    boolean existsByRuleName(String ruleName);

    // For update: check if rule name exists for another entity (exclude current by
    // id)
    boolean existsByRuleNameAndIdNot(String ruleName, Long id);
}
