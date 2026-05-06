-- DUMMY DATA FINAL-PROJECT-OOVP2026

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

-- USERS
INSERT INTO users (id, name, email, password) VALUES
(1, 'Fattah Fadli', 'fattahfadli09@gmail.com', '$2y$10$PatahPatah'),
(2, 'Azizah Putri', 'iamputri2930@gmail.com', '$2y$10$JijahJijah'),
(3, 'Ziaa', 'yesi.nurfitriyani@student.president.ac.id', '$2y$10$ZiaaZiaa'),
(4, 'Keefi Almer', 'keefi.firdaus@student.president.ac.id', '$2y$10$AlmerAlmer'),
(5, 'Alfairaz Putra', 'alfairaz.anantar@student.president.ac.id', '$2y$10$FairazFairaz');

-- MEMBERSHIPS
INSERT INTO memberships (id, user_id, tier, since) VALUES
(1, 1, 'Silver', '2026-01-10'),
(2, 2, 'Gold', '2026-02-01'),
(3, 3, 'Silver', '2026-03-15'),
(4, 4, 'Platinum', '2026-01-20'),
(5, 5, 'Gold', '2026-02-25');

-- MOVIES
INSERT INTO movies (id, title, genre, duration, rating, price, year, synopsis) VALUES
(1, 'Jumbo', 'Animation/Adventure', 102, 9.10, 50000, 2025, 'Film animasi Indonesia tentang Don dan pertunjukan bakat desa.'),
(2, 'Pabrik Gula', 'Horror', 118, 8.20, 45000, 2025, 'Teror misterius di sebuah pabrik gula tua.'),
(3, 'Petaka Gunung Gede', 'Horror', 109, 7.90, 45000, 2025, 'Pendakian berubah menjadi tragedi supranatural.'),
(4, 'Agak Laen: Menyala Pantiku!', 'Comedy', 125, 8.80, 55000, 2025, 'Empat polisi absurd menjalankan misi penyamaran.'),
(5, 'How to Train Your Dragon', 'Fantasy/Adventure', 126, 8.90, 65000, 2025, 'Live-action petualangan Hiccup dan Toothless.');

-- SHOWTIMES
INSERT INTO showtimes (id, movie_id, date, time, studio, format, price_mod) VALUES
(1, 1, '2026-05-10', '13:15:00', 'Studio 1', 'Regular', 0),
(2, 1, '2026-05-10', '18:45:00', 'Studio 1', 'IMAX', 20000),
(3, 2, '2026-05-10', '20:30:00', 'Studio 3', 'Regular', 0),
(4, 3, '2026-05-11', '19:15:00', 'Studio 4', 'Regular', 0),
(5, 4, '2026-05-11', '16:00:00', 'Studio 2', 'Regular', 0),
(6, 5, '2026-05-12', '14:30:00', 'Studio IMAX', 'IMAX', 25000),
(7, 5, '2026-05-12', '20:00:00', 'Studio IMAX', 'IMAX', 25000);

-- SNACKS
INSERT INTO snacks (id, name, price, icon) VALUES
(1, 'Popcorn Caramel', 55000, '🍿'),
(2, 'Coca Cola', 25000, '🥤'),
(3, 'French Fries', 35000, '🍟'),
(4, 'Hotdog', 40000, '🌭'),
(5, 'Nachos Cheese', 45000, '🧀');

-- BOOKINGS
INSERT INTO bookings (id, user_id, showtime_id, total_paid, status) VALUES
-- Fattah: nonton IMAX + snack besar
(1, 1, 2, 175000, 'active'),
-- Azizah: romance/comedy casual
(2, 2, 5, 55000, 'active'),
-- Ziaa: horror marathon
(3, 3, 3, 90000, 'expired'),
(4, 3, 4, 85000, 'active'),
-- Keefi: IMAX enthusiast
(5, 4, 6, 220000, 'active'),
(6, 4, 7, 180000, 'active'),
-- Fairaz: hemat + regular movie
(7, 5, 1, 50000, 'cancelled');

-- BOOKING SEATS
INSERT INTO booking_seats (booking_id, seat_code) VALUES
(1, 'E5'),
(1, 'E6'),
(2, 'C3'),
(3, 'H8'),
(4, 'G6'),
(5, 'D4'),
(5, 'D5'),
(6, 'F1'),
(6, 'F2'),
(7, 'A1');

-- PAYMENTS
INSERT INTO payments (id, booking_id, method, amount) VALUES
(1, 1, 'QRIS', 175000),
(2, 2, 'Debit Card', 55000),
(3, 3, 'GoPay', 90000),
(4, 4, 'ShopeePay', 85000),
(5, 5, 'QRIS', 220000),
(6, 6, 'Credit Card', 180000),
(7, 7, 'Cash', 50000);

-- SNACK ORDERS
INSERT INTO snack_orders (booking_id, snack_id, qty) VALUES
-- Fattah
(1, 1, 2),
(1, 2, 2),
-- Azizah
(2, 2, 1),
-- Ziaa
(3, 1, 1),
(4, 3, 1),
-- Keefi
(5, 1, 2),
(5, 4, 2),
(6, 5, 1),
-- Fairaz
(7, 2, 1);

-- REVIEWS
INSERT INTO reviews (id, user_id, movie_id, rating, text) VALUES
(1, 1, 5, 9.50,
'IMAX-nya keren banget, naga dan sound effect-nya gila.'),
(2, 2, 4, 8.70,
'Lucu banget. Satu studio ketawa terus.'),
(3, 3, 2, 8.20,
'Horror-nya dapet, apalagi jumpscare pabriknya.'),
(4, 3, 3, 7.90,
'Atmosfer gunungnya bikin tegang.'),
(5, 4, 5, 9.80,
'Worth every rupiah buat IMAX.'),
(6, 4, 1, 8.90,
'Animasi lokal sekelas Pixar sih ini.'),
(7, 5, 1, 8.00,
'Cocok buat ditonton rame-rame.');
