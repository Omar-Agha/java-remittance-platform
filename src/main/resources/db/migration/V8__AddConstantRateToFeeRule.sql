-- Migration to add the constant_rate column to the fee_rule table

ALTER TABLE fee_rule
ADD COLUMN constant_rate NUMERIC(10, 6) CHECK (constant_rate >= 0.0 AND constant_rate <= 1.0);