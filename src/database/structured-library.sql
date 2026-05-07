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

-- ERASE OLD TABLES (SAFE ORDER)
DROP TABLE IF EXISTS Issued_Book, Member, Member_Type, Book;

-- 1. Book
CREATE TABLE IF NOT EXISTS Book (
  Book_ID VARCHAR(10) PRIMARY KEY,
  Book_Title VARCHAR(255) NOT NULL,
  Book_Author VARCHAR(255) NOT NULL,
  Book_Category VARCHAR(255) NOT NULL,
  ISBN VARCHAR(13) NOT NULL UNIQUE,
  Book_Stock INT NOT NULL DEFAULT 1,
  Book_Status TINYINT(1) NOT NULL DEFAULT 1, -- 1 = available, 0 = loaned/unavailable
  INDEX idx_book_isbn (ISBN)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. Member_Type (lookup table for member categories and loan rules)
CREATE TABLE IF NOT EXISTS Member_Type (
  Member_Type_ID VARCHAR(10) PRIMARY KEY,
  Type_Name VARCHAR(255) NOT NULL UNIQUE,     -- e.g. 'student', 'lecturer', 'guest'
  Loan_Days INT NOT NULL DEFAULT 14,           -- default loan duration in days
  Loan_Limit INT NOT NULL DEFAULT 3,           -- max concurrent loans
  Fine_Per_Day DECIMAL(8,2) NOT NULL DEFAULT 0.50
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. Member
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

-- 4. Issued_Book
CREATE TABLE IF NOT EXISTS Issued_Book (
  Issued_ID VARCHAR(10) PRIMARY KEY,
  Book_ID VARCHAR(10) NOT NULL, -- FK -> Book.Book_ID
  Member_ID VARCHAR(10) NOT NULL, -- FK -> Member.Member_ID
  Date_issued DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  Date_returned DATETIME NULL NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

-- Example for student, lecturer, guest member types with different loan rules
/*
INSERT INTO Member_Type (Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day)
VALUES
  ('student', 14, 5, 0.50),
  ('lecturer', 30, 10, 0.25),
  ('guest', 7, 2, 1.00)
ON DUPLICATE KEY UPDATE
  Loan_Days = VALUES(Loan_Days),
  Loan_Limit = VALUES(Loan_Limit),
  Fine_Per_Day = VALUES(Fine_Per_Day);
*/

-- Example: ensure default Member_Type_ID exists (set default to 'guest' if desired)
-- (Optional) set default member type to 'guest' if ID differs from 1:
