-- Insert sample data into orders
INSERT INTO orders (code, date, total_price, customer_id, item_id, quantity)
VALUES
('1111', '2024-07-18', 15000.00,
    (SELECT id FROM customers WHERE name = 'Andi Santoso'),
    (SELECT id FROM items WHERE name = 'Laptop ASUS'),
    1),
('1321', '2024-07-20', 7000.00,
    (SELECT id FROM customers WHERE name = 'Budi Hartono'),
    (SELECT id FROM items WHERE name = 'Smartphone Samsung'),
    2),
('4411', '2024-07-22', 12000.00,
    (SELECT id FROM customers WHERE name = 'Cindy Wijaya'),
    (SELECT id FROM items WHERE name = 'Tablet Apple'),
    1);