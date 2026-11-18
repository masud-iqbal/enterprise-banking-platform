
CREATE TABLE customers
(
    id         UUID                     NOT NULL,
    first_name VARCHAR(100)             NOT NULL,
    last_name  VARCHAR(100)             NOT NULL,
    email      VARCHAR(150)             NOT NULL,
    phone      VARCHAR(20),
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    status     VARCHAR(20) DEFAULT 'ACTIVE' NOT NULL,

    CONSTRAINT pk_customers PRIMARY KEY (id),
    CONSTRAINT uk_customer_email UNIQUE (email)
);
