
USE book_mall;

SELECT
    category,
    title,
    price
FROM (
         SELECT
             category,
             title,
             price,
             RANK() OVER (PARTITION BY category ORDER BY price DESC) AS price_rank
         FROM
             books
     ) t
WHERE
    price_rank = 1;
