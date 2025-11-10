CREATE DATABASE hotel_db;
USE hotel_db;

CREATE TABLE rooms (
    room_id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50),
    price DOUBLE,
    is_booked BOOLEAN DEFAULT FALSE
);

INSERT INTO rooms (category, price, is_booked) VALUES
('Standard', 1500, FALSE),
('Standard', 1500, FALSE),
('Deluxe', 2500, FALSE),
('Deluxe', 2500, FALSE),
('Suite', 4000, FALSE);

CREATE TABLE reservations (
    reservation_id INT PRIMARY KEY AUTO_INCREMENT,
    guest_name VARCHAR(100),
    room_id INT,
    check_in VARCHAR(20),
    check_out VARCHAR(20),
    payment_mode VARCHAR(50),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id)
);
