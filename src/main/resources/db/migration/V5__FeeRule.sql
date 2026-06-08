-- Migration for FeeRule and FeeRuleLine tables

CREATE TABLE fee_rule (
    id BIGSERIAL PRIMARY KEY,
    effective_date TIMESTAMP NOT NULL,
    rule_name VARCHAR(255) NOT NULL UNIQUE,
    fee_rule_type VARCHAR(255) NOT NULL,      -- Must be NOT NULL, matches the Java entity
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    approved_by_user_id BIGINT,
    approved_by_user_name VARCHAR(255)
);

CREATE TABLE fee_rule_lines (
    id BIGSERIAL PRIMARY KEY,
    fee_rule_id BIGINT NOT NULL REFERENCES fee_rule(id) ON DELETE CASCADE,
    amount NUMERIC(19, 2),
    percentage NUMERIC(5, 2)
);

-- 
-- Validation:
-- 1. fee_rule_type is NOT NULL in entity and flow, so mark as NOT NULL here.
-- 2. rule_name is unique and NOT NULL, matching entity.
-- 3. is_approved default/nullable is correct.
-- 4. fee_rule_lines.fee_rule_id used in OneToMany in entity, so ON DELETE CASCADE is suitable.
-- 5. Line amounts are optional - matches Java allowing null for amount/percentage.