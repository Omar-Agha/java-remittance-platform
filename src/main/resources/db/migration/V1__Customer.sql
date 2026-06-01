CREATE TABLE customers (
    id BIGSERIAL PRIMARY KEY,
    national_number VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone_number VARCHAR(255),
    birth_date DATE,
    address VARCHAR(255),
    nationality VARCHAR(255),
    is_active BOOLEAN
);