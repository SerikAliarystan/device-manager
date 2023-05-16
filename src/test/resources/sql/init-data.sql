UPDATE device_tb SET status = 'BOOKED' WHERE id = 1;

INSERT INTO order_tb (id, device_id, user_id, order_date)
VALUES (1, 1, 1, timestamp '2023-01-01 01:01:01'),
       (2, 1, 1, timestamp '2023-02-02 02:02:02'),
       (3, 1, 1, CURRENT_TIMESTAMP());