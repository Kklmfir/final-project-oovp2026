-- DUMMY DATA FINAL-PROJECT-OOVP2026

-- ERASE OLD DATA (OPTIONAL - if tables exist)
DELETE FROM Book;
DELETE FROM Member;
DELETE FROM Member_Type;
DELETE FROM Issued_Book;

-- DISABLE FOREIGN KEY CHECKS TO AVOID CONSTRAINT ERRORS DURING INSERTION
SET FOREIGN_KEY_CHECKS = 0;

-- 1. Book
INSERT INTO Book (Book_ID, Book_Title, Book_Author, ISBN, Book_Category, Book_Status, Book_Stock) VALUES
    ('BK001', 'Clean Code', 'Robert C. Martin', '9780132350884', 'Programming', 1, 5),
    ('BK002', 'The Clean Coder', 'Robert C. Martin', '978013781073', 'Programming', 1, 3),
    ('BK003', 'Code Complete', 'Steve McConnell', '9780735619678', 'Programming', 1, 4),
    ('BK004', 'Refactoring', 'Martin Fowler', '9780201485677', 'Programming', 1, 2),
    ('BK005', 'Design Patterns', 'Erich Gamma', '9780201633610', 'Software Engineering', 1, 3),
    ('BK006', 'The Pragmatic Programmer', 'Andrew Hunt', '9780201616224', 'Programming', 1, 2),
    ('BK007', 'Introduction to Algorithms', 'Thomas H. Cormen', '9780262033848', 'Computer Science', 1, 3),
    ('BK008', 'Artificial Intelligence: A Modern Approach', 'Stuart Russell and Peter Norvig', '9780136042594', 'Artificial Intelligence', 1, 2),
    ('BK009', 'Deep Learning', 'Ian Goodfellow, Yoshua Bengio, and Aaron Courville', '9780262035613', 'Machine Learning', 1, 2),
    ('BK010', 'Computer Networking: A Top-Down Approach', 'James F. Kurose and Keith W. Ross', '9780133594140', 'Networking', 1, 2),
    ('BK011', 'Operating System Concepts', 'Abraham Silberschatz, Peter B. Galvin, and Greg Gagne', '9781119456339', 'Operating System', 1, 2),
    ('BK012', 'Cybersecurity Essentials', 'Charles J. Brooks, Christopher Grow, Philip Craig, and Donald Shorter Jr.', '9781119362395','Cybersecurity' ,0, 1),
    ('BK013', 'Think and Grow Rich','Napoleon Hill','9781585424337','Motivation' ,1, 2),
    ('BK014', 'Linux Basics for Hackers','OccupyTheWeb','9781593278557','Linux' ,0, 1),
    ('BK015', 'The Art of Computer Programming','Donald E. Knuth','9780201896831','Computer Science' ,1, 3),
    ('BK016', 'Cracking the Coding Interview','Gayle Laakmann McDowell','9780984782857','Programming' ,1, 2);

-- 2. Member_Type
INSERT INTO Member_Type (Member_Type_ID, Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day) VALUES
    /*
    ('STD001', 7, 5, 0.50),
    ('LEC001', 14, 10, 0.25),
    ('GST001', 7, 2, 1.00);
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

-- 3. Member
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

-- 4. Issued_Book
INSERT INTO Issued_Book (Issued_ID, Book_ID, Member_ID, Date_issued, Date_returned) VALUES
    -- STUDENTS
    ('IS001', 'BK001', 'STU2026001', '2026-04-10 09:00:00', '2026-04-15 10:00:00'), -- Keefi Almer Firdaus borrows "Clean Code"
    ('IS002', 'BK002', 'STU2026002', '2026-04-11 10:00:00', '2026-04-17 11:00:00'), -- Alfairaz Putra Anantar borrows "The Clean Coder"
    ('IS003', 'BK003', 'STU2026003', '2026-04-12 11:00:00', '2026-04-17 12:00:00'), -- Azizah Putri Susanto borrows "Code Complete"
    ('IS004', 'BK004', 'STU2026004', '2026-04-13 12:00:00', '2026-04-20 13:00:00'), -- Yesi Nurfitriyani borrows "Refactoring"
    ('IS005', 'BK005', 'STU2026005', '2026-04-14 13:00:00', '2026-04-19 14:00:00'), -- M Fattah Fadli Rohman Putra borrows "Design Patterns"
    -- LECTURERS
    ('IS006', 'BK006', 'LEC2026001', '2026-04-15 14:00:00', '2026-05-15 15:00:00'), -- Dr. Keefi A. Firdaus borrows "The Pragmatic Programmer"
    ('IS007', 'BK007', 'LEC2026002', '2026-04-16 15:00:00', '2026-05-16 16:00:00'), -- Prof. Alfairaz P. Anantar borrows "Introduction to Algorithms"
    ('IS008', 'BK008', 'LEC2026003', '2026-04-17 16:00:00', '2026-05-17 17:00:00'), -- Dr. Azizah P. Susanto borrows "Artificial Intelligence: A Modern Approach"
    ('IS009', 'BK009', 'LEC2026004', '2026-04-18 17:00:00', '2026-05-18 18:00:00'), -- Dr. Yesi N. Fitriyani borrows "Deep Learning"
    ('IS010', 'BK010', 'LEC2026005', '2026-04-19 18:00:00', '2026-05-19 19:00:00'), -- Prof. Fattah R. Rohman borrows "Computer Networking: A Top-Down Approach"
    -- GUESTS
    ('IS011', 'BK011', 'GST2026003', '2026-04-20 19:00:00', '2026-04-27 20:00:00'), -- Keefi Almer borrows "Operating System Concepts"
    ('IS012', 'BK012', 'GST2026005', '2026-04-21 20:00:00', '2026-04-28 21:00:00'), -- Alfairaz Putra borrows "Cybersecurity Essentials"
    ('IS013', 'BK013', 'GST2026004', '2026-04-22 21:00:00', '2026-04-29 22:00:00'), -- Azizah Putri borrows "Think and Grow Rich"
    ('IS014', 'BK014', 'GST2026001', '2026-04-23 22:00:00', '2026-04-30 23:00:00'), -- Yesi N. Fitri borrows "Linux Basics for Hackers"
    ('IS015', 'BK015', 'GST2026002', '2026-04-24 23:00:00', '2026-05-01 08:00:00'); -- Fattah Rohman borrows "The Art of Computer Programming"

-- RE-ENABLE FOREIGN KEY CHECKS AFTER INSERTION
SET FOREIGN_KEY_CHECKS = 1;
