package com.omar.remittance_management.modules.feeRule.service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.omar.remittance_management.common.exception.BusinessException;
import com.omar.remittance_management.modules.feeRule.dto.FeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.SaveFeeRuleDto;
import com.omar.remittance_management.modules.feeRule.dto.SaveFeeRuleLineDto;
import com.omar.remittance_management.modules.feeRule.entity.FeeRule;
import com.omar.remittance_management.modules.feeRule.enums.FeeRuleType;
import com.omar.remittance_management.modules.feeRule.mapper.FeeRuleMapper;
import com.omar.remittance_management.modules.feeRule.repository.FeeRuleRepo;

@ExtendWith(MockitoExtension.class)
class FeeRuleServiceTest {

    @Mock
    private FeeRuleRepo feeRuleRepo;

    @Mock
    private FeeRuleMapper feeRuleMapper;

    @InjectMocks
    private FeeRuleCrudService feeRuleCrudService;

    @Nested
    @DisplayName("createFeeRule")
    class CreateFeeRule {

        @Test
        void creates_new_fee_rule_and_returns_dto() {

            SaveFeeRuleDto saveDto = new SaveFeeRuleDto(
                    Instant.now().plusSeconds(3600),
                    "TestRule",
                    FeeRuleType.Range,
                    List.of(
                            new SaveFeeRuleLineDto(BigDecimal.valueOf(100.0), BigDecimal.valueOf(10.0)),
                            new SaveFeeRuleLineDto(BigDecimal.valueOf(200.0), BigDecimal.valueOf(20.0))));

            ArgumentCaptor<FeeRule> savedRule = ArgumentCaptor.forClass(FeeRule.class);

            when(feeRuleRepo.existsByRuleName(saveDto.ruleName())).thenReturn(false);
            when(feeRuleRepo.save(any(FeeRule.class))).thenAnswer(invocation -> {
                return invocation.getArgument(0);
            });

            FeeRuleDto result = feeRuleCrudService.createFeeRule(saveDto);

            verify(feeRuleRepo).existsByRuleName(saveDto.ruleName());
            verify(feeRuleRepo).save(savedRule.capture());

            FeeRule saved = savedRule.getValue();
            assertThat(saved.getRuleName()).isEqualTo(saveDto.ruleName());
            assertThat(saved.getFeeRuleType()).isEqualTo(saveDto.feeRuleType());
            assertThat(saved.getLines()).hasSize(saveDto.lines().size());
            assertThat(saved.getLines().get(0).getAmount()).isEqualTo(saveDto.lines().get(0).amount());
            verify(feeRuleMapper).toDto(any(FeeRule.class));
        }

        @Test
        void throws_when_rule_name_is_blank() {

            SaveFeeRuleDto saveDto = new SaveFeeRuleDto(
                    Instant.now().plusSeconds(3600),
                    " ",
                    FeeRuleType.Constant,
                    Collections.emptyList());

            assertThatThrownBy(() -> feeRuleCrudService.createFeeRule(saveDto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("must not be null or blank");
        }

        @Test
        void throws_when_rule_name_not_unique() {
            SaveFeeRuleDto saveDto = new SaveFeeRuleDto(Instant.now().plusSeconds(3600), "Existing",
                    FeeRuleType.Constant, Collections.emptyList());
            when(feeRuleRepo.existsByRuleName("Existing")).thenReturn(true);
            assertThatThrownBy(() -> feeRuleCrudService.createFeeRule(saveDto))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("must be unique");
        }

        @Test
        void throws_when_fee_rule_type_null() {
            SaveFeeRuleDto saveDto = new SaveFeeRuleDto(Instant.now().plusSeconds(3600), "X", null,
                    null);
            assertThatThrownBy(() -> feeRuleCrudService.createFeeRule(saveDto))
                    .isInstanceOf(BusinessException.class);
        }

        @Nested
        class UpdateFeeRule {
            @Test
            void updates_fields_if_provided() {
                FeeRule feeRule = new FeeRule("old", FeeRuleType.Range);
                feeRule.setEffectiveDate(Instant.now().plusSeconds(50000),
                        Instant.now());
                when(feeRuleRepo.findById(1L)).thenReturn(Optional.of(feeRule));
                when(feeRuleRepo.existsByRuleNameAndIdNot("new", 1L)).thenReturn(false);

                SaveFeeRuleDto updateDto = new SaveFeeRuleDto(Instant.now().plusSeconds(80000), "new",
                        FeeRuleType.Constant,
                        null);

                when(feeRuleRepo.save(any(FeeRule.class))).thenAnswer(invocation -> invocation.getArgument(0));
                FeeRuleDto resultDto = new FeeRuleDto(1L, updateDto.effectiveDate(), "new", FeeRuleType.Constant,
                        List.of());
                when(feeRuleMapper.toDto(any(FeeRule.class))).thenReturn(resultDto);

                FeeRuleDto result = feeRuleCrudService.updateFeeRule(1L, updateDto);

                assertThat(result).isNotNull();
                assertThat(result.ruleName()).isEqualTo("new");
                assertThat(result.feeRuleType()).isEqualTo(FeeRuleType.Constant);

            }

            @Test
            void update_approved_rule() {
                FeeRule feeRule = new FeeRule("old", FeeRuleType.Range);
                feeRule.setEffectiveDate(Instant.now().plusSeconds(50000),
                        Instant.now());
                feeRule.approve(2L, "userName", Instant.now());
                when(feeRuleRepo.findById(1L)).thenReturn(Optional.of(feeRule));
                when(feeRuleRepo.existsByRuleNameAndIdNot("new", 1L)).thenReturn(false);

                SaveFeeRuleDto updateDto = new SaveFeeRuleDto(Instant.now().plusSeconds(80000), "new",
                        FeeRuleType.Constant,
                        null);
                // Assert that feeRuleRepo.save should never be called, because an approved
                // FeeRule should cause an exception before save
                assertThatThrownBy(() -> feeRuleCrudService.updateFeeRule(1L, updateDto))
                        .isInstanceOf(BusinessException.class)
                        .hasMessageContaining("approved");
                // verify(feeRuleRepo, never()).save(any(FeeRule.class));

            }
        }

        @Test
        void throws_when_rule_name_duplicate() {
            FeeRule entity = new FeeRule("orig", FeeRuleType.Constant);
            when(feeRuleRepo.findById(2L)).thenReturn(Optional.of(entity));
            when(feeRuleRepo.existsByRuleNameAndIdNot("dup", 2L)).thenReturn(true);
            SaveFeeRuleDto saveDto = new SaveFeeRuleDto(Instant.now().plusSeconds(1000), "dup", FeeRuleType.Constant,

                    null);
            assertThatThrownBy(() -> feeRuleCrudService.updateFeeRule(2L, saveDto))
                    .isInstanceOf(BusinessException.class);
        }

    }
}
