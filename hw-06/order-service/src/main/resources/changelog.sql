CREATE TABLE IF NOT EXISTS idempotency_key_pool(
    idempotency_key TEXT NOT NULL UNIQUE,
    username        TEXT NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS orders(
    id              SERIAL PRIMARY KEY,
    idempotency_key TEXT NOT NULL UNIQUE,
    pickup          TEXT NOT NULL,
    destination     TEXT,
    ordered_by      TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS inx_orders_ordered_by ON orders (ordered_by);
