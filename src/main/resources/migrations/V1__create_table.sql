-- The first migration file to create main table

CREATE TABLE IF NOT EXISTS users (
    id         uuid PRIMARY KEY,
    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    is_active  BOOLEAN NOT NULL
);