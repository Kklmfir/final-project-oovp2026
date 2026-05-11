-- DUMMY DATA FINAL-PROJECT-OOVP2026

-- ERASE OLD DATA (OPTIONAL - if tables exist)
DELETE FROM Author_Book;
DELETE FROM Book;
DELETE FROM Category;
DELETE FROM Issued_Book;
DELETE FROM Member;
DELETE FROM Member_Type;
DELETE FROM Publisher;

-- DISABLE FOREIGN KEY CHECKS TO AVOID CONSTRAINT ERRORS DURING INSERTION
SET FOREIGN_KEY_CHECKS = 0;

-- Book
INSERT INTO Book (Book_ID, Book_Title, Book_Author_ID, Book_Category_ID, Book_Publisher_ID, Book_Year, ISBN, Book_Stock, Book_Status) VALUES
    ('BK001', 'Laskar Pelangi', 'AUTH001', 'CAT001', 'PUB001', 2005, '9789793062792', 6, 1), -- Available
    ('BK002', 'Sang Pemimpi', 'AUTH001', 'CAT001', 'PUB001', 2006, '9789791227209', 5, 1), -- Available
    ('BK003', 'Bumi Manusia', 'AUTH002', 'CAT003', 'PUB009', 1980, '9789799731234', 8, 1), -- Available
    ('BK004', 'Anak Semua Bangsa', 'AUTH002', 'CAT003', 'PUB009', 1981, '9789799731241', 6, 0), -- Borrowed
    ('BK005', 'Negeri 5 Menara', 'AUTH003', 'CAT009', 'PUB002', 2009, '9789791227209', 4, 1), -- Available
    ('BK006', 'Ranah 3 Warna', 'AUTH003', 'CAT009', 'PUB002', 2011, '9789792248616', 3, 0), -- Borrowed
    ('BK007', 'Ayat-Ayat Cinta', 'AUTH004', 'CAT004', 'PUB003', 2004, '9789793210605', 7, 1), -- Available
    ('BK008', 'Ketika Cinta Bertasbih', 'AUTH004', 'CAT004', 'PUB003', 2007, '9789791102162', 5, 0), -- Borrowed
    ('BK009', 'Bumi', 'AUTH005', 'CAT005', 'PUB004', 2014, '9786020332956', 5, 1), -- Available
    ('BK010', 'Bulan', 'AUTH005', 'CAT005', 'PUB004', 2015, '9786020351179', 3, 1), -- Available
    ('BK011', 'Supernova: Ksatria, Puteri dan Bintang Jatuh', 'AUTH006', 'CAT005', 'PUB005', 2001, '9789799625700', 6, 1), -- Available
    ('BK012', 'Perahu Kertas', 'AUTH008', 'CAT008', 'PUB002', 2009, '9789791227216', 4, 1), -- Available
    ('BK013', 'Atomic Habits', 'AUTH009', 'CAT002', 'PUB002', 2019, '9786020633176', 4, 1), -- Available
    ('BK014', 'Filosofi Teras', 'AUTH010', 'CAT006', 'PUB006', 2018, '9786024125189', 6, 1), -- Available
    ('BK015', 'Belajar Dasar Python', 'AUTH007', 'CAT007', 'PUB010', 2023, '9786231234567', 2, 0); -- Borrowed

-- Member_Type
INSERT INTO Member_Type (Member_Type_ID, Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day) VALUES
    /*
    ('STD001', 7, 5, 0.50),
    ('LEC001', 14, 10, 0.25),
    ('GST001', 7, 3, 1.00);
    ON DUPLICATE KEY UPDATE
        Loan_Days = VALUES(Loan_Days),
        Loan_Limit = VALUES(Loan_Limit),
        Fine_Per_Day = VALUES(Fine_Per_Day);
    */
    -- STUDENTS
    ('STD001', 'Keefi Almer Firdaus', 7, 5, 0.50),
    ('STD002', 'Alfairaz Putra Anantar', 7, 5, 0.50),
    ('STD003', 'Azizah Putri Susanto', 7, 5, 0.50),
    ('STD004', 'Yesi Nurfitriyani', 7, 5, 0.50),
    ('STD005', 'M Fattah Fadli Rohman Putra', 7, 5, 0.50),
    -- LECTURERS
    ('LEC001', 'Dr. Keefi A. Firdaus', 14, 10, 0.25),
    ('LEC002', 'Prof. Alfairaz P. Anantar', 14, 10, 0.25),
    ('LEC003', 'Dr. Azizah P. Susanto', 14, 10, 0.25),
    ('LEC004', 'Dr. Yesi N. Fitriyani', 14, 10, 0.25),
    ('LEC005', 'Prof. Fattah R. Rohman', 14, 10, 0.25),
    -- GUESTS
    ('GST001', 'Keefi Almer', 7, 3, 1.00),
    ('GST002', 'Alfairaz Putra', 7, 3, 1.00),
    ('GST003', 'Azizah Putri', 7, 3, 1.00),
    ('GST004', 'Yesi N. Fitri', 7, 3, 1.00),
    ('GST005', 'Fattah Rohman', 7, 3, 1.00);

-- Member
INSERT INTO Member (Member_ID, Member_Name, Register_Date, Member_Type_ID, Identifier, Email, Phone) VALUES
    -- STUDENTS
    ('STU2026001', 'Keefi Almer Firdaus', '2026-04-01 08:15:00', 1, 'STD001', 'keefi.firdaus@student.president.ac.id', 081381110001),
    ('STU2026002', 'Alfairaz Putra Anantar', '2026-04-01 08:30:00', 1, 'STD002', 'alfairaz.anantar@student.president.ac.id', 081381110002),
    ('STU2026003', 'Azizah Putri Susanto', '2026-04-01 08:45:00', 1, 'STD003', 'azizah.susanto@student.president.ac.id', 081381110003),
    ('STU2026004', 'Yesi Nurfitriyani', '2026-04-01 09:00:00', 1, 'STD004', 'yesi.fitriyani@student.president.ac.id', 081381110004),
    ('STU2026005', 'M Fattah Fadli Rohman Putra', '2026-04-01 09:15:00', 1, 'STD005', 'fattah.putra@student.president.ac.id', 081381110005),
    -- LECTURERS
    ('LEC2026001', 'Dr. Keefi A. Firdaus', '2026-04-02 10:00:00', 2, 'LEC001', 'keefi.firdaus@president.ac.id', 081381110011),
    ('LEC2026002', 'Prof. Alfairaz P. Anantar', '2026-04-02 10:20:00', 2, 'LEC002', 'alfairaz.anantar@president.ac.id', 081381110012),
    ('LEC2026003', 'Dr. Azizah P. Susanto', '2026-04-02 10:40:00', 2, 'LEC003', 'azizah.susanto@president.ac.id', 081381110013),
    ('LEC2026004', 'Dr. Yesi N. Fitriyani', '2026-04-02 11:00:00', 2, 'LEC004', 'yesi.fitriyani@president.ac.id', 081381110014),
    ('LEC2026005', 'Prof. Fattah R. Rohman', '2026-04-02 11:20:00', 2, 'LEC005', 'fattah.rohman@president.ac.id', 081381110015),
    -- GUESTS
    ('GST2026001', 'Keefi Almer', '2026-04-03 11:40:00', 3, 'GST001', 'keefi.almer@outlook.com', 081381110021),
    ('GST2026002', 'Alfairaz Putra', '2026-04-03 12:20:00', 3, 'GST002', 'alfairaz.putra@gmail.com', 081381110022),
    ('GST2026003', 'Azizah Putri', '2026-04-03 12:00:00', 3, 'GST003', 'azizah.putri@outlook.com', 081381110023),
    ('GST2026004', 'Yesi N. Fitri', '2026-04-03 11:00:00', 3, 'GST004', 'yesi.fitri@gmail.com', 081381110024),
    ('GST2026005', 'Fattah Rohman', '2026-04-03 11:20:00', 3, 'GST005', 'fattah.rohman@yahoo.com', 081381110025);

-- Issued_Book
INSERT INTO Issued_Book (Issued_ID, Book_ID, Member_ID, Date_issued, Date_returned) VALUES
    -- STUDENTS
    ('ISB001', 'BK001', 'STU2026001', '2026-04-10 09:00:00', '2026-04-15 10:00:00'), -- Keefi Almer Firdaus borrows "Clean Code"
    ('ISB002', 'BK002', 'STU2026002', '2026-04-11 10:00:00', '2026-04-17 11:00:00'), -- Alfairaz Putra Anantar borrows "The Clean Coder"
    ('ISB003', 'BK003', 'STU2026003', '2026-04-12 11:00:00', '2026-04-17 12:00:00'), -- Azizah Putri Susanto borrows "Code Complete"
    ('ISB004', 'BK004', 'STU2026004', '2026-04-13 12:00:00', '2026-04-20 13:00:00'), -- Yesi Nurfitriyani borrows "Refactoring"
    ('ISB005', 'BK005', 'STU2026005', '2026-04-14 13:00:00', '2026-04-19 14:00:00'), -- M Fattah Fadli Rohman Putra borrows "Design Patterns"
    -- LECTURERS
    ('ISB006', 'BK006', 'LEC2026001', '2026-04-15 14:00:00', '2026-05-15 15:00:00'), -- Dr. Keefi A. Firdaus borrows "The Pragmatic Programmer"
    ('ISB007', 'BK007', 'LEC2026002', '2026-04-16 15:00:00', '2026-05-16 16:00:00'), -- Prof. Alfairaz P. Anantar borrows "Introduction to Algorithms"
    ('ISB008', 'BK008', 'LEC2026003', '2026-04-17 16:00:00', '2026-05-17 17:00:00'), -- Dr. Azizah P. Susanto borrows "Artificial Intelligence: A Modern Approach"
    ('ISB009', 'BK009', 'LEC2026004', '2026-04-18 17:00:00', '2026-05-18 18:00:00'), -- Dr. Yesi N. Fitriyani borrows "Deep Learning"
    ('ISB010', 'BK010', 'LEC2026005', '2026-04-19 18:00:00', '2026-05-19 19:00:00'), -- Prof. Fattah R. Rohman borrows "Computer Networking: A Top-Down Approach"
    -- GUESTS
    ('ISB011', 'BK011', 'GST2026003', '2026-04-20 19:00:00', '2026-04-27 20:00:00'), -- Keefi Almer borrows "Operating System Concepts"
    ('ISB012', 'BK012', 'GST2026005', '2026-04-21 20:00:00', '2026-04-28 21:00:00'), -- Alfairaz Putra borrows "Cybersecurity Essentials"
    ('ISB013', 'BK013', 'GST2026004', '2026-04-22 21:00:00', '2026-04-29 22:00:00'), -- Azizah Putri borrows "Think and Grow Rich"
    ('ISB014', 'BK014', 'GST2026001', '2026-04-23 22:00:00', '2026-04-30 23:00:00'), -- Yesi N. Fitri borrows "Linux Basics for Hackers"
    ('ISB015', 'BK015', 'GST2026002', '2026-04-24 23:00:00', '2026-05-01 08:00:00'); -- Fattah Rohman borrows "The Art of Computer Programming"

-- Category
INSERT INTO Category (Category_ID, Category_Name, Description) VALUES
    ('CAT001', 'Novels', 'Books that contain fictional stories, often with a narrative structure and character development.'),
    ('CAT002', 'Self Improvement', 'Books that provide guidance and advice on personal growth, motivation, and achieving success.'),
    ('CAT003', 'History', 'Books that cover historical events, periods, and figures.'),
    ('CAT004', 'Religion', 'Books that explore religious beliefs, practices, and texts.'),
    ('CAT005', 'Fantasy', 'Books that feature imaginary worlds, creatures, and magic.'),
    ('CAT006', 'Psychology', 'Books that examine the human mind, behavior, and mental processes.'),
    ('CAT007', 'Technology', 'Books that focus on technological advancements and innovations.'),
    ('CAT008', 'Romance', 'Books that explore themes of love, relationships, and emotional connections.'),
    ('CAT009', 'Education', 'Books that provide knowledge and skills for educational purposes.'),
    ('CAT010', 'Business', 'Books that cover topics related to business management, entrepreneurship, and economics.');

-- Publisher
INSERT INTO Publisher (Publisher_ID, Publisher_Name, Publisher_Contact) VALUES
    ('PUB001', 'Bentang Pustaka', 'cs@bentangpustaka.com'),
    ('PUB002', 'Gramedia Pustaka Utama', 'marketing@gpu.id'),
    ('PUB003', 'Republika Penerbit', 'info@republika.co.id'),
    ('PUB004', 'Tere Liye Publishing', 'admin@tereliye.com'),
    ('PUB005', 'Supernova Publisher', 'contact@truedee.com'),
    ('PUB006', 'Kompas', 'redaksi@kompas.id'),
    ('PUB007', 'Elex Media Komputindo', 'cs@elexmedia.co.id'),
    ('PUB008', 'Mizan', 'redaksi@mizan.com'),
    ('PUB009', 'Lentera Dipantara', 'info@lentera-dipantara.or.id'),
    ('PUB010', 'Bukunesia', 'support@bukunesia.id');

-- Author_Book
INSERT INTO Author_Book (Author_Book_ID, Author_Book_Name, Author_Book_Contact) VALUES
    ('AUTHB001', 'Andrea Hirata', 'andrea.hirata@bentangpustaka.com'),
    ('AUTHB002', 'Pramoedya Ananta Toer', 'pramoedya@lentera-dipantara.or.id'),
    ('AUTHB003', 'Ahmad Fuadi', 'ahmad.fuadi@menaramedia.id'),
    ('AUTHB004', 'Habiburrahman El Shirazy', 'kangabik@shirazyinstitute.com'),
    ('AUTHB005', 'Tere Liye', 'admin@tereliye.com'),
    ('AUTHB006', 'Dewi Lestari', 'dee@truedee.com'),
    ('AUTHB007', 'Dian Nafi', 'dian.nafi@bukunesia.id'),
    ('AUTHB008', 'Dewi Lestari', 'dee@truedee.com'),
    ('AUTHB009', 'James Clear', 'team@jamesclear.com'),
    ('AUTHB010', 'Henry Manampiring', 'henry.manampiring@kompasgramedia.com');

-- Additional Features
DELETE FROM Author_Journal;
DELETE FROM Institute;
DELETE FROM Journal;
DELETE FROM Issued_Journal;
DELETE FROM Author_Audio_Book;
DELETE FROM Audio_Book;
DELETE FROM Issued_Audio_Book;

--- Author_Journal
INSERT INTO Author_Journal (Author_Journal_ID, Author_Journal_Name, Author_Journal_Contact) VALUES
    ('AUTHJ001', 'Dr. Keefi A. Firdaus', 'keefi.firdaus@president.ac.id'),
    ('AUTHJ002', 'Prof. Alfairaz P. Anantar', 'alfairaz.anantar@president.ac.id'),
    ('AUTHJ003', 'Dr. Azizah P. Susanto', 'azizah.susanto@president.ac.id'),
    ('AUTHJ004', 'Dr. Yesi N. Fitriyani', 'yesi.fitriyani@president.ac.id'),
    ('AUTHJ005', 'Prof. Fattah R. Rohman', 'fattah.rohman@president.ac.id');

-- Institute
INSERT INTO Institute (Institute_ID, Institute_Name, Institute_Contact, Institute_City) VALUES
    ('INST001', 'President University', 'info@president.ac.id', 'Cikarang'),
    ('INST002', 'Institut Teknologi Sains Bandung', 'info@itsb.ac.id', 'Cikarang'),
    ('INST003', 'Universitas Indonesia', 'info@ui.ac.id', 'Jakarta'),
    ('INST004', 'Institut Teknologi Bandung', 'info@itb.ac.id', 'Bandung'),
    ('INST005', 'Universitas Gadjah Mada', 'info@ugm.ac.id', 'Yogyakarta');

-- Journal
INSERT INTO Journal (Journal_ID, Journal_Title, Journal_Year, Journal_Author_ID, Journal_Publisher_ID, Journal_Institute_ID, Journal_City, Journal_Index) VALUES
    ('JRN001', 'Journal of Computer Science', 2020, 'AUTHJ001', 'PUB002', 'INST001', 'Cikarang', 'Scopus 2'),
    ('JRN002', 'International Journal of MIAW', 2021, 'AUTHJ002', 'PUB003', 'INST002', 'Cikarang', 'Sinta 2'),
    ('JRN003', 'How To Be A Good Meong', 2019, 'AUTHJ003', 'PUB004', 'INST003', 'Jakarta', 'Sinta 5'),
    ('JRN004', 'Best Solution for Icikiwir Upgrades', 2022, 'AUTHJ004', 'PUB005', 'INST004', 'Bandung', 'Scopus 3'),
    ('JRN005', 'Cybersecurity Using Banks Security', 2023, 'AUTHJ005', 'PUB006', 'INST005', 'Yogyakarta', 'Scopus 1');

-- Issued_Journal
INSERT INTO Issued_Journal (Issued_Journal_ID, Journal_ID, Member_ID, Date_Issued, Date_Returned) VALUES
    ('ISJ001', 'JRN001', 'STU2026001', '2026-04-10 09:00:00', '2026-04-15 10:00:00'), -- Keefi Almer Firdaus borrows "Journal of Computer Science"
    ('ISJ002', 'JRN002', 'LEC2026002', '2026-04-16 15:00:00', '2026-05-16 16:00:00'), -- Prof. Alfairaz P. Anantar borrows "International Journal of MIAW"
    ('ISJ003', 'JRN003', 'GST2026003', '2026-04-20 19:00:00', '2026-04-27 20:00:00'), -- Keefi Almer borrows "How To Be A Good Meong"
    ('ISJ004', 'JRN004', 'LEC2026004', '2026-04-18 17:00:00', '2026-05-18 18:00:00'), -- Dr. Yesi N. Fitriyani borrows "Best Solution for Icikiwir Upgrades"
    ('ISJ005', 'JRN005', 'GST2026005', '2026-04-21 20:00:00', '2026-04-28 21:00:00'); -- Alfairaz Putra borrows "Cybersecurity Using Banks Security"

-- Author_Audio_Book
INSERT INTO Author_Audio_Book (Author_Audio_Book_ID, Author_Audio_Book_Name, Author_Audio_Book_Contact) VALUES
    ('AUTHAB001', 'Miaw Miaw', 'Miaw@miaw.com'),
    ('AUTHAB002', 'Meong Meong', 'Meong@meong.com'),
    ('AUTHAB003', 'Purr Purr', 'Purr@purr.com'),
    ('AUTHAB004', 'Icikiwir Icikiwir', 'Icikiwir@icikiwir.com'),
    ('AUTHAB005', 'Kicau Mania', 'Kicau@kicau.com');

-- Audio_Book
INSERT INTO Audio_Book (Audio_Book_ID, Audio_Book_Title, Audio_Book_Author_ID, Audio_Book_Category_ID, Audio_Book_Publisher_ID, Audio_Book_Year, ISBN, Audio_Book_Duration, Audio_Book_Format, Audio_Book_Status) VALUES
    ('ABK001', 'Sad Miaw Story', 'AUTHAB001', 'CAT001', 'PUB001', 2020, '9789793062792', 3600, 'MP3', 1), -- Available
    ('ABK002', 'The Best Part of Meong', 'AUTHAB002', 'CAT002', 'PUB002', 2021, '9789791227209', 5400, 'AAC', 0), -- Borrowed
    ('ABK003', 'How to Call Chicken', 'AUTHAB003', 'CAT003', 'PUB003', 2022, '9789799731234', 4200, 'WAV', 1), -- Available
    ('ABK004', 'The Adventures of Icikiwir', 'AUTHAB004', 'CAT004', 'PUB004', 2023, '9789799731241', 4800, 'MP3', 1), -- Available
    ('ABK005', 'The Secrets of Kicau Mania', 'AUTHAB005', 'CAT005', 'PUB005', 2024, '9789791227209', 3000, 'AAC', 0); -- Borrowed

-- Issued_Audio_Book
INSERT INTO Issued_Audio_Book (Issued_Audio_Book_ID, Audio_Book_ID, Member_ID, Date_Issued, Date_Returned) VALUES
    ('ISAB001', 'ABK001', 'STU2026001', '2026-04-10 09:00:00', '2026-04-15 10:00:00'), -- Keefi Almer Firdaus borrows "Sad Miaw Story"
    ('ISAB002', 'ABK002', 'LEC2026002', '2026-04-16 15:00:00', '2026-05-16 16:00:00'), -- Prof. Alfairaz P. Anantar borrows "The Best Part of Meong"
    ('ISAB003', 'ABK003', 'GST2026003', '2026-04-20 19:00:00', '2026-04-27 20:00:00'), -- Keefi Almer borrows "How to Call Chicken"
    ('ISAB004', 'ABK004', 'LEC2026004', '2026-04-18 17:00:00', '2026-05-18 18:00:00'), -- Dr. Yesi N. Fitriyani borrows "The Adventures of Icikiwir"
    ('ISAB005', 'ABK005', 'GST2026005', '2026-04-21 20:00:00', '2026-04-28 21:00:00'); -- Alfairaz Putra borrows "The Secrets of Kicau Mania"

-- RE-ENABLE FOREIGN KEY CHECKS AFTER INSERTION
SET FOREIGN_KEY_CHECKS = 1;
