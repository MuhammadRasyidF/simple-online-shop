CREATE TABLE items (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code BINARY(16) NOT NULL,
    stock INTEGER DEFAULT 0,
    price DECIMAL(10, 2),
    is_available BOOLEAN DEFAULT TRUE,
    last_re_stock DATE
);
