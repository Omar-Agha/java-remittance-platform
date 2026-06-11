-- Migration for currencies table

CREATE TABLE currencies (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(3) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    symbol VARCHAR(5),
    active BOOLEAN DEFAULT TRUE
);