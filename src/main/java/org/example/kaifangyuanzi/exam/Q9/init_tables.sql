CREATE DATABASE IF NOT EXISTS book_mall DEFAULT CHARACTER SET utf8mb4;
USE book_mall;

DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE books (
                       book_id INT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(100) NOT NULL,
                       category VARCHAR(50) NOT NULL,
                       price DECIMAL(10, 2) NOT NULL,
                       stock INT NOT NULL
);

CREATE TABLE orders (
                        order_id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
                             item_id INT AUTO_INCREMENT PRIMARY KEY,
                             order_id INT NOT NULL,
                             book_id INT NOT NULL,
                             quantity INT NOT NULL,
                             buy_price DECIMAL(10, 2) NOT NULL,
                             FOREIGN KEY (order_id) REFERENCES orders(order_id),
                             FOREIGN KEY (book_id) REFERENCES books(book_id),
                             CHECK (quantity > 0)
);

INSERT INTO users (username) VALUES
                                 ('张三'),
                                 ('李四'),
                                 ('王五');

INSERT INTO books (title, category, price, stock) VALUES
                                                      ('Java编程思想', 'Tech', 99.00, 100),
                                                      ('MySQL入门到精通', 'Tech', 59.00, 200),
                                                      ('计算机网络', 'Tech', 79.00, 150),
                                                      ('明朝那些事儿', 'History', 45.00, 300),


                                                      ('人类简史', 'History', 68.00, 250),
                                                      ('三体', 'Fiction', 32.00, 500);

INSERT INTO orders (user_id, total_amount) VALUES
                                               (1, 237.00),
                                               (1, 45.00),
                                               (2, 138.00),
                                               (3, 100.00);

INSERT INTO order_items (order_id, book_id, quantity, buy_price) VALUES
                                                                     (1, 1, 1, 99.00),
                                                                     (1, 2, 1, 59.00),
                                                                     (1, 3, 1, 79.00),
                                                                     (2, 4, 1, 45.00),
                                                                     (3, 1, 1, 99.00),
                                                                     (3, 2, 1, 39.00),
                                                                     (4, 6, 2, 50.00);
