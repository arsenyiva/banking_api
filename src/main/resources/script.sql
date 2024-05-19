DROP TABLE if exists bank_accounts;
DROP TABLE if exists Users;
CREATE TABLE IF NOT EXISTS Users
(

    user_id       SERIAL PRIMARY KEY,
    username      VARCHAR(100) NOT NULL,
    password      VARCHAR(100) NOT NULL,
    date_of_birth DATE         NOT NULL,
    phone         VARCHAR(20),
    extra_phone   VARCHAR(20),
    email         VARCHAR(100),
    extra_email   VARCHAR(100)

    );

CREATE TABLE IF NOT EXISTS bank_accounts
(
    id      SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DOUBLE PRECISION,
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    );
