-- Migration to create iss_rem table
-- This table matches the IssRem entity in Java

CREATE TABLE iss_rem (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    customer_phone VARCHAR(255) NOT NULL,
    customer_full_name VARCHAR(255) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL CHECK (amount >= 0.01),
    currency_code VARCHAR(16),
    currency_id BIGINT NOT NULL,
    issue_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    approved_at TIMESTAMP(6) WITH TIME ZONE,
    is_cashed BOOLEAN NOT NULL DEFAULT FALSE,
    cashed_at TIMESTAMP(6) WITH TIME ZONE,
    beneficiary_name VARCHAR(255) NOT NULL,
    beneficiary_phone VARCHAR(255)
);

-- All columns required by the entity class are present
-- - Field types and nullability match
-- - Default values and constraints reflect entity defaults and validation