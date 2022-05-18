CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS balance;
DROP TABLE IF EXISTS currency;
DROP TABLE IF EXISTS account;

CREATE TABLE IF NOT EXISTS account (
    id uuid DEFAULT uuid_generate_v4 (),
    customer_id uuid NOT NULL,
    country varchar(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS currency (
    id uuid DEFAULT uuid_generate_v4 (),
    symbol VARCHAR(10),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS balance (
    id uuid DEFAULT uuid_generate_v4 (),
    account_id uuid REFERENCES account (id),
    currency_id uuid REFERENCES currency (id),
    amount decimal(12,2),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transaction (
    id uuid DEFAULT uuid_generate_v4 (),
    account_id uuid REFERENCES account (id),
    amount decimal(12,2),
    currency_id uuid REFERENCES currency (id),
    direction VARCHAR(10),
    description VARCHAR(255),
    PRIMARY KEY (id)
);