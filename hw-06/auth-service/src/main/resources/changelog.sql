CREATE TABLE IF NOT EXISTS account(
    id       SERIAL PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL UNIQUE
);
