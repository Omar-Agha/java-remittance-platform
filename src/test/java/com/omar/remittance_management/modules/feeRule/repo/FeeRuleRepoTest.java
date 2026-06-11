package com.omar.remittance_management.modules.feeRule.repo;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;
import com.omar.remittance_management.modules.feeRule.repository.FeeRuleRepo;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FeeRuleRepoTest {

        @Autowired
        private FeeRuleRepo feeRuleRepo;

        private static final Instant NOW = Instant.now().plusSeconds(365 * 24 * 60 * 60);

        private FeeRule createAndSaveFeeRule(String ruleName, FeeRuleType type,
                        Instant effectiveDate, boolean isApproved) {
                var previousNow = effectiveDate.minusSeconds(2 * 24 * 60 * 60);
                FeeRule feeRule = new FeeRule(ruleName, type);
                feeRule.setEffectiveDate(effectiveDate, previousNow);

                // Set approval fields directly to simulate various cases

                if (isApproved) {
                        feeRule.approve(1L, "admin", previousNow);
                }
                return feeRuleRepo.save(feeRule);
        }

        @Nested
        @DisplayName("findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc")
        class FindFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc {

                @Test
                void finds_approved_and_effective_rule() {
                        // Define a Clock instance so tests can control time if needed for testing
                        // Instant-related logic.
                        // (Not used elsewhere in this file yet, but establishing for future test
                        // stability.)

                        FeeRule rule1 = createAndSaveFeeRule("Rule 1", FeeRuleType.Constant,
                                        NOW.minusSeconds(3600), true);
                        FeeRule rule2 = createAndSaveFeeRule("Rule 2", FeeRuleType.Constant,
                                        NOW.minusSeconds(1800), true);
                        // Unapproved rule should not be found:
                        createAndSaveFeeRule("Rule 3", FeeRuleType.Constant, NOW.minusSeconds(900),
                                        false);

                        assertThat(feeRuleRepo.count()).isEqualTo(3);

                        Optional<FeeRule> found = feeRuleRepo
                                        .findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                                                        NOW);
                        assertThat(found).isPresent();
                        assertThat(found.get().getRuleName()).isEqualTo("Rule 2");
                }

                @Test
                void returns_empty_when_no_approved_rule() {

                        createAndSaveFeeRule("Unavailable", FeeRuleType.Constant,
                                        NOW.minusSeconds(7200), false);

                        Optional<FeeRule> found = feeRuleRepo
                                        .findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                                                        NOW);
                        assertThat(found).isEmpty();
                }

                @Test
                void does_not_return_future_rules() {

                        createAndSaveFeeRule("FutureRule", FeeRuleType.Constant,
                                        NOW.plusSeconds(5000), true);

                        Optional<FeeRule> found = feeRuleRepo
                                        .findFirstByIsApprovedTrueAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                                                        NOW);
                        assertThat(found).isEmpty();
                }
        }

        @Nested
        @DisplayName("existsByRuleName")
        class ExistsByRuleName {
                @Test
                void returns_true_when_rule_name_exists() {

                        createAndSaveFeeRule("TestRule", FeeRuleType.Constant, NOW, true);
                        assertThat(feeRuleRepo.existsByRuleName("TestRule")).isTrue();
                }

                @Test
                void returns_false_when_rule_name_missing() {
                        assertThat(feeRuleRepo.existsByRuleName("MissingRule")).isFalse();
                }
        }

        @Nested
        @DisplayName("existsByRuleNameAndIdNot")
        class ExistsByRuleNameAndIdNot {
                @Test
                void returns_true_when_another_entity_has_same_rule_name() {

                        FeeRule rule = createAndSaveFeeRule("DuplicateName", FeeRuleType.Constant,
                                        NOW, true);

                        assertThat(feeRuleRepo.existsByRuleNameAndIdNot("DuplicateName", rule.getId()
                                        + 1L)).isTrue();
                }

                @Test
                void returns_false_for_self_id() {

                        FeeRule rule = createAndSaveFeeRule("UniqueName", FeeRuleType.Constant,
                                        NOW, true);

                        assertThat(feeRuleRepo.existsByRuleNameAndIdNot("UniqueName",
                                        rule.getId())).isFalse();
                }
        }

}
