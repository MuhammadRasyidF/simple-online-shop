CREATE TABLE orders (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    date DATE,
    total_price DECIMAL(10, 2) NOT NULL,
    customer_id INTEGER,
    item_id INTEGER,
    quantity INTEGER,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (item_id) REFERENCES items(id)
);
