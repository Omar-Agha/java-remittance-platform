CREATE TABLE address (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    address1 VARCHAR(255) NOT NULL,
    address2 VARCHAR(255),
    description VARCHAR(255),
    "order" INTEGER NOT NULL CHECK ("order" >= 0),
    customer_id BIGINT NOT NULL,
    CONSTRAINT fk_address_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE INDEX idx_address_customer_id ON address(customer_id);