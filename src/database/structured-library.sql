-- ONLY RUN IN LOCAL DATABASE
-- DROP & CREATE DATABASE (MySQL / MariaDB)
-- DROP DATABASE IF EXISTS `final-project-oovp2026`;
-- CREATE DATABASE IF NOT EXISTS `final-project-oovp2026`;
--   DEFAULT CHARACTER SET = utf8mb4
--   DEFAULT COLLATE = utf8mb4_unicode_ci;
-- USE `final-project-oovp2026`;

-- (PostgreSQL alternative)
-- DROP DATABASE IF EXISTS "final-project-oovp2026";
-- CREATE DATABASE "final-project-oovp2026";
-- \c "final-project-oovp2026";

-- DISABLE FOREIGN KEY CHECKS TO AVOID CONSTRAINT ERRORS DURING INSERTION
SET FOREIGN_KEY_CHECKS = 0;

-- Additional Feature
DROP TABLE IF EXISTS Author_Journal, Institute, Journal, Issued_Journal, Author_Audio_Book, Audio_Book;

-- ERASE OLD TABLES
DROP TABLE IF EXISTS Issued_Book, Member, Member_Type, Book, Author_Book, Category, Publisher;

-- Author_Book
CREATE TABLE IF NOT EXISTS Author_Book (
  Author_Book_ID VARCHAR(10) PRIMARY KEY,
  Author_Book_Name VARCHAR(255) NOT NULL UNIQUE,
  Author_Book_Contact VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Category
CREATE TABLE IF NOT EXISTS Category (
  Category_ID VARCHAR(10) PRIMARY KEY,
  Category_Name VARCHAR(255) NOT NULL UNIQUE,
  Description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Publisher
CREATE TABLE IF NOT EXISTS Publisher (
  Publisher_ID VARCHAR(10) PRIMARY KEY,
  Publisher_Name VARCHAR(255) NOT NULL UNIQUE,
  Publisher_Contact VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Member_Type (lookup table for member categories and loan rules)
CREATE TABLE IF NOT EXISTS Member_Type (
  Member_Type_ID VARCHAR(10) PRIMARY KEY,
  Type_Name VARCHAR(255) NOT NULL UNIQUE,     -- e.g. 'student', 'lecturer', 'guest'
  Loan_Days INT NOT NULL DEFAULT 14,           -- default loan duration in days
  Loan_Limit INT NOT NULL DEFAULT 3,           -- max concurrent loans
  Fine_Per_Day DECIMAL(8,2) NOT NULL DEFAULT 0.50
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Example for student, lecturer, guest member types with different loan rules
/*
INSERT INTO Member_Type (Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day)
VALUES
  ('student', 14, 5, 0.50),
  ('lecturer', 30, 10, 0.25),
  ('guest', 7, 3, 1.00)
ON DUPLICATE KEY UPDATE
  Loan_Days = VALUES(Loan_Days),
  Loan_Limit = VALUES(Loan_Limit),
  Fine_Per_Day = VALUES(Fine_Per_Day);
*/

-- Member
CREATE TABLE IF NOT EXISTS Member (
  Member_ID VARCHAR(10) PRIMARY KEY,
  Member_Name VARCHAR(255) NOT NULL,
  Register_Date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Member_Type_ID VARCHAR(10) NOT NULL, -- FK -> Member_Type.Member_Type_ID
    -- optional
  Identifier VARCHAR(100), -- student ID / staff ID / guest code
  Email VARCHAR(255),
  Phone VARCHAR(13),
  INDEX idx_member_type (Member_Type_ID),
  CONSTRAINT fk_member_member_type FOREIGN KEY (Member_Type_ID)
    REFERENCES Member_Type(Member_Type_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Book
CREATE TABLE IF NOT EXISTS Book (
  Book_ID VARCHAR(10) PRIMARY KEY,
  Book_Title VARCHAR(255) NOT NULL,
  Book_Author_ID VARCHAR(10) NOT NULL, -- FK -> Author.Author_Book_ID
  Book_Category_ID VARCHAR(10) NOT NULL, -- FK -> Category.Category_ID
  Book_Publisher_ID VARCHAR(10), -- FK -> Publisher.Publisher_ID
  Book_Year INT NOT NULL,
  ISBN VARCHAR(13) NOT NULL UNIQUE,
  Book_Stock INT NOT NULL DEFAULT 1,
  Book_Status TINYINT(1) NOT NULL DEFAULT 1, -- 1 = available, 0 = loaned/unavailable
  INDEX idx_book_isbn (ISBN),
  INDEX idx_book_category (Book_Category_ID),
  INDEX idx_book_author (Book_Author_ID),
  INDEX idx_book_publisher (Book_Publisher_ID),
  CONSTRAINT fk_book_category FOREIGN KEY (Book_Category_ID)
    REFERENCES Category(Category_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_book_author FOREIGN KEY (Book_Author_ID)
    REFERENCES Author_Book(Author_Book_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_book_publisher FOREIGN KEY (Book_Publisher_ID)
    REFERENCES Publisher(Publisher_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Issued_Book
CREATE TABLE IF NOT EXISTS Issued_Book (
  Issued_ID VARCHAR(10) PRIMARY KEY,
  Book_ID VARCHAR(10) NOT NULL, -- FK -> Book.Book_ID
  Member_ID VARCHAR(10) NOT NULL, -- FK -> Member.Member_ID
  Date_issued DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Due_Date DATETIME NULL,
  Actual_Return_Date DATETIME NULL,
  Date_returned DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_issued_book (Book_ID),
  INDEX idx_issued_member (Member_ID),
  CONSTRAINT fk_issued_book_book FOREIGN KEY (Book_ID)
    REFERENCES Book(Book_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_issued_book_member FOREIGN KEY (Member_ID)
    REFERENCES Member(Member_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Example: ensure default Member_Type_ID exists (set default to 'guest' if desired)
-- (Optional) set default member type to 'guest' if ID differs from 1

-- Author_Journal
CREATE TABLE IF NOT EXISTS Author_Journal (
  Author_Journal_ID VARCHAR(10) PRIMARY KEY,
  Author_Journal_Name VARCHAR(255) NOT NULL,
  Author_Journal_Contact VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Institute
CREATE TABLE IF NOT EXISTS Institute (
  Institute_ID VARCHAR(10) PRIMARY KEY,
  Institute_Name VARCHAR(255) NOT NULL,
  Institute_Contact VARCHAR(255) NOT NULL,
  Institute_City VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Journal
CREATE TABLE IF NOT EXISTS Journal (
  Journal_ID VARCHAR(10) PRIMARY KEY,
  Journal_Title VARCHAR(255) NOT NULL,
  Journal_Year INT NOT NULL,
  Journal_Author_ID VARCHAR(10) NOT NULL, -- FK -> Author_Journal.Author_Journal_ID
  Journal_Publisher_ID VARCHAR(10) NOT NULL, -- FK -> Publisher.Publisher_ID
  Journal_Institute_ID VARCHAR(10), -- FK -> Institute.Institute_ID
  Journal_City VARCHAR(255),
  Journal_Index VARCHAR(255),
  INDEX idx_journal_author_id (Journal_Author_ID),
  INDEX idx_journal_publisher_id (Journal_Publisher_ID),
  INDEX idx_journal_institute_id (Journal_Institute_ID),
  CONSTRAINT fk_journal_author_id FOREIGN KEY (Journal_Author_ID)
    REFERENCES Author_Journal(Author_Journal_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_journal_publisher_id FOREIGN KEY (Journal_Publisher_ID)
    REFERENCES Publisher(Publisher_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_journal_institute_id FOREIGN KEY (Journal_Institute_ID)
    REFERENCES Institute(Institute_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Issued_Journal
CREATE TABLE IF NOT EXISTS Issued_Journal (
  Issued_Journal_ID VARCHAR(10) PRIMARY KEY,
  Journal_ID VARCHAR(10) NOT NULL, -- FK -> Journal.Journal_ID
  Member_ID VARCHAR(10) NOT NULL, -- FK -> Member.Member_ID
  Date_issued DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Date_returned DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_issued_journal (Journal_ID),
  INDEX idx_issued_member (Member_ID),
  CONSTRAINT fk_issued_journal_journal FOREIGN KEY (Journal_ID)
    REFERENCES Journal(Journal_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_issued_journal_member FOREIGN KEY (Member_ID)
    REFERENCES Member(Member_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Author_Audio_Book
CREATE TABLE IF NOT EXISTS Author_Audio_Book (
  Author_Audio_Book_ID VARCHAR(10) PRIMARY KEY,
  Author_Audio_Book_Name VARCHAR(255) NOT NULL,
  Author_Audio_Book_Contact VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Audio_Book
CREATE TABLE IF NOT EXISTS Audio_Book (
  Audio_Book_ID VARCHAR(10) PRIMARY KEY,
  Audio_Book_Title VARCHAR(255) NOT NULL,
  Audio_Book_Author_ID VARCHAR(10) NOT NULL, -- FK -> Author_Audio_Book.Author_Audio_Book_ID
  Audio_Book_Category_ID VARCHAR(10) NOT NULL, -- FK -> Category.Category_ID
  Audio_Book_Publisher_ID VARCHAR(10), -- FK -> Publisher.Publisher_ID
  Audio_Book_Year INT NOT NULL,
  ISBN VARCHAR(13) NOT NULL UNIQUE,
  Audio_Book_Duration INT NOT NULL, -- duration in seconds
  Audio_Book_Format VARCHAR(50) NOT NULL, -- e.g. 'MP3', 'AAC', 'WAV'
  Audio_Book_Status TINYINT(1) NOT NULL DEFAULT 1, -- 1 = available, 0 = loaned/unavailable
  INDEX idx_audio_book_isbn (ISBN),
  INDEX idx_audio_book_author (Audio_Book_Author_ID),
  INDEX idx_audio_book_category (Audio_Book_Category_ID),
  INDEX idx_audio_book_publisher (Audio_Book_Publisher_ID),
  CONSTRAINT fk_audio_book_author FOREIGN KEY (Audio_Book_Author_ID)
    REFERENCES Author_Audio_Book(Author_Audio_Book_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_audio_book_category FOREIGN KEY (Audio_Book_Category_ID)
    REFERENCES Category(Category_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_audio_book_publisher FOREIGN KEY (Audio_Book_Publisher_ID)
    REFERENCES Publisher(Publisher_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Issued_Audio_Book
CREATE TABLE IF NOT EXISTS Issued_Audio_Book (
  Issued_Audio_Book_ID VARCHAR(10) PRIMARY KEY,
  Audio_Book_ID VARCHAR(10) NOT NULL, -- FK -> Audio_Book.Audio_Book_ID
  Member_ID VARCHAR(10) NOT NULL, -- FK -> Member.Member_ID
  Date_issued DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Date_returned DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_issued_audio_book (Audio_Book_ID),
  INDEX idx_issued_member (Member_ID),
  CONSTRAINT fk_issued_audio_book_audio_book FOREIGN KEY (Audio_Book_ID)
    REFERENCES Audio_Book(Audio_Book_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  CONSTRAINT fk_issued_audio_book_member FOREIGN KEY (Member_ID)
    REFERENCES Member(Member_ID)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- RE-ENABLE FOREIGN KEY CHECKS AFTER INSERTION
SET FOREIGN_KEY_CHECKS = 1;
