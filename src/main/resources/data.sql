-- 1. Seed Users
MERGE INTO users (id, name) KEY(id) VALUES (1, 'Leanne Graham');
MERGE INTO users (id, name) KEY(id) VALUES (2, 'Ervin Howell');
MERGE INTO users (id, name) KEY(id) VALUES (3, 'Clementine Bauch');
MERGE INTO users (id, name) KEY(id) VALUES (4, 'Patricia Lebsack');
MERGE INTO users (id, name) KEY(id) VALUES (5, 'Chelsey Dietrich');

-- 2. Seed Accounts
MERGE INTO accounts (id, user_id, balance, on_hold, daily_limit, currency) KEY(id) VALUES (1, 1, 20000.00, 0.00, 50000.00, 'PHP');
MERGE INTO accounts (id, user_id, balance, on_hold, daily_limit, currency) KEY(id) VALUES (2, 2, 30000.00, 0.00, 50000.00, 'PHP');
MERGE INTO accounts (id, user_id, balance, on_hold, daily_limit, currency) KEY(id) VALUES (3, 3, 40000.00, 0.00, 50000.00, 'PHP');
MERGE INTO accounts (id, user_id, balance, on_hold, daily_limit, currency) KEY(id) VALUES (4, 4, 50000.00, 0.00, 50000.00, 'PHP');
MERGE INTO accounts (id, user_id, balance, on_hold, daily_limit, currency) KEY(id) VALUES (5, 5, 60000.00, 0.00, 50000.00, 'PHP');

-- 3. Seed Contacts
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (1, 1, 2);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (2, 1, 3);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (3, 1, 4);

MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (4, 2, 1);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (5, 2, 3);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (6, 2, 4);

MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (7, 3, 1);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (8, 3, 2);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (9, 3, 4);

MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (10, 4, 1);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (11, 4, 2);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (12, 4, 5);

MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (13, 5, 1);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (14, 5, 2);
MERGE INTO contacts (id, user_id, contact_user_id) KEY(id) VALUES (15, 5, 3);

-- INSERT INTO TRANSACTIONS (id, sender_account_id, receiver_account_id, currency, amount, status, date, idempotency_key) VALUES(1, 1, 2, 'PHP', 1000.00, 'COMPLETED', '2026-05-17 20:02:00.0', '123')
