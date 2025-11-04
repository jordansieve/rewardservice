INSERT INTO customer (id, name) VALUES ('1', 'populatedtransactions');
INSERT INTO customer (id, name) VALUES ('2', 'lesstransactions');
INSERT INTO customer (id, name) VALUES ('3', 'notransactions');

INSERT INTO transaction (id, customer_id, date, amount) VALUES ('1', '1', '2025-08-10', 15);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('2', '1', '2025-08-15', 85);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('3', '1', '2025-09-08', 10);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('4', '1', '2025-09-13', 110);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('5', '1', '2025-10-12', 20);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('6', '1', '2025-10-18', 120);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('7', '1', '2025-11-09', 35);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('8', '1', '2025-11-14', 125);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('9', '1', '2025-12-11', 50);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('10', '1', '2025-12-16', 150);

INSERT INTO transaction (id, customer_id, date, amount) VALUES ('11', '2', '2025-10-22', 110);
INSERT INTO transaction (id, customer_id, date, amount) VALUES ('12', '2', '2025-11-03', 90);