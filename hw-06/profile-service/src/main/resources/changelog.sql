CREATE TABLE IF NOT EXISTS profile(
    id          SERIAL PRIMARY KEY,
    username    TEXT   NOT NULL UNIQUE,
    first_name  TEXT,
    last_name   TEXT,
    email       TEXT,
    phone       TEXT
);
