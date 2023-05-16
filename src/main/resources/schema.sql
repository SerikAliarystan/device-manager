
CREATE TABLE device_tb (
  id INT AUTO_INCREMENT NOT NULL,
   device_name VARCHAR(50),
   status VARCHAR(255),
   version INT,
   CONSTRAINT pk_device_tb PRIMARY KEY (id)
);

CREATE TABLE order_tb (
  id INT AUTO_INCREMENT NOT NULL,
   device_id INT NOT NULL,
   user_id INT NOT NULL,
   order_date TIMESTAMP NOT NULL,
   CONSTRAINT pk_order_tb PRIMARY KEY (id)
);

CREATE TABLE user_tb (
  id INT AUTO_INCREMENT NOT NULL,
   user_name VARCHAR(50),
   CONSTRAINT pk_user_tb PRIMARY KEY (id)
);

ALTER TABLE order_tb ADD CONSTRAINT FK_ORDER_TB_ON_DEVICE FOREIGN KEY (device_id) REFERENCES device_tb (id);

ALTER TABLE order_tb ADD CONSTRAINT FK_ORDER_TB_ON_USER FOREIGN KEY (user_id) REFERENCES user_tb (id);