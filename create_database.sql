-- MySQL script for customer_db
-- @author y.adel

CREATE DATABASE IF NOT EXISTS customer_db;
USE customer_db;

CREATE TABLE IF NOT EXISTS customers (
    id         INT PRIMARY KEY AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    phone      VARCHAR(50),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Optional sample data
INSERT INTO customers (name, email, phone) VALUES 
('Youssef Adel', 'yadel@text.com', '+123456789'),
('Ahmed Adel', 'aadel@test.com', '+987654321');