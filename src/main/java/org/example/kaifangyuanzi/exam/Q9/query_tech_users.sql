
USE book_mall;

SELECT
    u.username,
    SUM(oi.quantity) AS total_tech_books,
    SUM(oi.quantity * oi.buy_price) AS total_spent
FROM
    users u
        JOIN orders o ON u.user_id = o.user_id
        JOIN order_items oi ON o.order_id = oi.order_id
        JOIN books b ON oi.book_id = b.book_id
WHERE
    b.category = 'Tech'
GROUP BY
    u.user_id, u.username
HAVING
    total_tech_books >= 3;
