-- 1. Seed Users
MERGE INTO users (id, name) KEY(id) VALUES (1, 'John Doe');

-- 2. Seed Wallets
MERGE INTO wallets (id, user_id, balance, currency, daily_limit) KEY(id) VALUES (1, 1, 5000.00, 'PHP', 50000.00);

-- 3. Tell H2 to start its automatic counters higher than the manual IDs
ALTER TABLE users ALTER COLUMN id RESTART WITH 2;
ALTER TABLE wallets ALTER COLUMN id RESTART WITH 2;