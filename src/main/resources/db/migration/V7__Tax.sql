-- Migration for taxes table

CREATE TABLE taxes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    rate NUMERIC(5, 4) NOT NULL CHECK (rate >= 0.00 AND rate <= 1.00),
    active BOOLEAN NOT NULL
);