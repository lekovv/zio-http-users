-- The first migration file to create main table

CREATE TABLE IF NOT EXISTS statuses (
    id             VARCHAR PRIMARY KEY,
    description    VARCHAR(255),
    active BOOLEAN NOT NULL
);
