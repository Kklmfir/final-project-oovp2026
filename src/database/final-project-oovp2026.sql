-- ONLY RUN IN LOCAL DATABASE
-- DROP & CREATE DATABASE (MySQL / MariaDB)
-- DROP DATABASE IF EXISTS `final-project-oovp2026`;
-- CREATE DATABASE `final-project-oovp2026`;
-- USE `final-project-oovp2026`;

-- (PostgreSQL alternative)
-- DROP DATABASE IF EXISTS "final-project-oovp2026";
-- CREATE DATABASE "final-project-oovp2026";
-- \c "final-project-oovp2026";

-- ERASE OLD DATA (OPTIONAL - if tables exist)
DELETE FROM snack_orders;
DELETE FROM booking_seats;
DELETE FROM payments;
DELETE FROM reviews;
DELETE FROM bookings;
DELETE FROM showtimes;
DELETE FROM memberships;
DELETE FROM snacks;
DELETE FROM movies;
DELETE FROM users;

-- ERASE OLD TABLES (SAFE ORDER)
DROP TABLE IF EXISTS
    snack_orders,
    booking_seats,
    payments,
    reviews,
    bookings,
    showtimes,
    memberships,
    snacks,
    movies,
    users;

-- CONFIG
SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';
START TRANSACTION;
SET time_zone = '+00:00';

-- TABLE: bookings
CREATE TABLE bookings (
    id INT(11) NOT NULL,
    user_id INT(11) DEFAULT NULL,
    showtime_id INT(11) DEFAULT NULL,
    total_paid INT(11) DEFAULT NULL,
    status ENUM('active','expired','cancelled') DEFAULT 'active',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: booking_seats
CREATE TABLE booking_seats (
    booking_id INT(11) NOT NULL,
    seat_code VARCHAR(5) NOT NULL
);

-- TABLE: memberships
CREATE TABLE memberships (
    id INT(11) NOT NULL,
    user_id INT(11) DEFAULT NULL,
    tier ENUM('Silver','Gold','Platinum') DEFAULT NULL,
    since DATE DEFAULT NULL
);

-- TABLE: movies
CREATE TABLE movies (
    id INT(11) NOT NULL,
    title VARCHAR(200) DEFAULT NULL,
    genre VARCHAR(100) DEFAULT NULL,
    duration INT(11) DEFAULT NULL,
    rating DECIMAL(2,1) DEFAULT NULL,
    price INT(11) DEFAULT NULL,
    year INT(11) DEFAULT NULL,
    synopsis TEXT DEFAULT NULL
);

-- TABLE: payments
CREATE TABLE payments (
    id INT(11) NOT NULL,
    booking_id INT(11) DEFAULT NULL,
    method VARCHAR(50) DEFAULT NULL,
    amount INT(11) DEFAULT NULL,
    paid_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: reviews
CREATE TABLE reviews (
    id INT(11) NOT NULL,
    user_id INT(11) DEFAULT NULL,
    movie_id INT(11) DEFAULT NULL,
    rating INT(11) DEFAULT NULL,
    text TEXT DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- TABLE: showtimes
CREATE TABLE showtimes (
    id INT(11) NOT NULL,
    movie_id INT(11) DEFAULT NULL,
    date DATE DEFAULT NULL,
    time TIME DEFAULT NULL,
    studio VARCHAR(50) DEFAULT NULL,
    format VARCHAR(20) DEFAULT NULL,
    price_mod INT(11) DEFAULT NULL
);

-- TABLE: snacks
CREATE TABLE snacks (
    id INT(11) NOT NULL,
    name VARCHAR(100) DEFAULT NULL,
    price INT(11) DEFAULT NULL,
    icon VARCHAR(10) DEFAULT NULL
);

-- TABLE: snack_orders
CREATE TABLE snack_orders (
    booking_id INT(11) NOT NULL,
    snack_id INT(11) NOT NULL,
    qty INT(11) DEFAULT NULL
);

-- TABLE: users
CREATE TABLE users (
    id INT(11) NOT NULL,
    name VARCHAR(100) DEFAULT NULL,
    email VARCHAR(100) DEFAULT NULL,
    password VARCHAR(255) DEFAULT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- INDEXES
ALTER TABLE bookings
    ADD PRIMARY KEY (id),
    ADD KEY idx_user_id (user_id),
    ADD KEY idx_showtime_id (showtime_id);

ALTER TABLE booking_seats
    ADD PRIMARY KEY (booking_id, seat_code);

ALTER TABLE memberships
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uq_user_id (user_id);

ALTER TABLE movies
    ADD PRIMARY KEY (id);

ALTER TABLE payments
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uq_booking_id (booking_id);

ALTER TABLE reviews
    ADD PRIMARY KEY (id),
    ADD KEY idx_user_id (user_id),
    ADD KEY idx_movie_id (movie_id);

ALTER TABLE showtimes
    ADD PRIMARY KEY (id),
    ADD KEY idx_movie_id (movie_id);

ALTER TABLE snacks
    ADD PRIMARY KEY (id);

ALTER TABLE snack_orders
    ADD PRIMARY KEY (booking_id, snack_id),
    ADD KEY idx_snack_id (snack_id);

ALTER TABLE users
    ADD PRIMARY KEY (id),
    ADD UNIQUE KEY uq_email (email);

-- AUTO INCREMENT
ALTER TABLE bookings     MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE memberships  MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE movies       MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE payments     MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE reviews      MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE showtimes    MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE snacks       MODIFY id INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE users        MODIFY id INT(11) NOT NULL AUTO_INCREMENT;

-- FOREIGN KEYS
ALTER TABLE bookings
    ADD CONSTRAINT fk_bookings_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    ADD CONSTRAINT fk_bookings_showtime
        FOREIGN KEY (showtime_id) REFERENCES showtimes(id);

ALTER TABLE booking_seats
    ADD CONSTRAINT fk_booking_seats_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(id);

ALTER TABLE memberships
    ADD CONSTRAINT fk_memberships_user
        FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE payments
    ADD CONSTRAINT fk_payments_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(id);

ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id) REFERENCES users(id),
    ADD CONSTRAINT fk_reviews_movie
        FOREIGN KEY (movie_id) REFERENCES movies(id);

ALTER TABLE showtimes
    ADD CONSTRAINT fk_showtimes_movie
        FOREIGN KEY (movie_id) REFERENCES movies(id);

ALTER TABLE snack_orders
    ADD CONSTRAINT fk_snack_orders_booking
        FOREIGN KEY (booking_id) REFERENCES bookings(id),
    ADD CONSTRAINT fk_snack_orders_snack
        FOREIGN KEY (snack_id) REFERENCES snacks(id);

COMMIT;