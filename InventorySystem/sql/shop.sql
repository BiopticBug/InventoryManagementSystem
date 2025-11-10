CREATE DATABASE shop;
USE shop;

-- USERS table (for login)
CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL
);

-- PRODUCTS table (for inventory)
CREATE TABLE IF NOT EXISTS products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sku VARCHAR(64) UNIQUE,
  name VARCHAR(255) NOT NULL,
  category VARCHAR(255) NOT NULL,
  price DOUBLE NOT NULL,
  quantity INT NOT NULL,
  description VARCHAR(255),
  stock INT NOT NULL
);

INSERT INTO users (username, password_hash, role) VALUES
('admin', 'admin123', 'ADMIN'),
('staff', 'staff123', 'STAFF');

INSERT INTO products (sku, name, category, price, quantity, description, stock) VALUES
('SKU001', 'Laptop', 'Electronics', 65000.00, 10, 'HP Laptop i5 8GB RAM', 10),
('SKU002', 'Wireless Mouse', 'Accessories', 500.00, 50, 'Logitech Wireless Mouse', 50),
('SKU003', 'Office Desk', 'Furniture', 7500.00, 5, 'Wooden office desk', 5),
('SKU004', 'USB Cable', 'Accessories', 150.00, 200, 'Type-C Cable', 200);


