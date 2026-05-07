# Final Project for OOVP - Semester 20252

Deskripsi singkat
-----------------
Proyek ini adalah Final Project untuk mata kuliah OOVP (Object-Oriented Programming/Visualization) Semester 20252. 
Aplikasi sederhana berbasis CRUD (Create, Read, Update, Delete) dibuat menggunakan Java (paradigma OOP) dengan IDE NetBeans (dapat dikembangkan juga di VSCode). Database utama menggunakan MySQL/MariaDB (lokal seperti XAMPP/Laragon) — opsi cloud dapat digunakan sesuai kebutuhan.

Report / Dokumentasi
--------------------
Laporan proyek dapat dibaca di:  
https://docs.google.com/document/d/1N-WFUDSEtVoejvnkfw3rEKwMEA0iAOyLlUE9DmewL1Y/edit?usp=sharing

Tujuan proyek
-------------
Membangun sebuah sistem manajemen perpustakaan sederhana dengan fitur dasar:
- Menambah dan mengelola data buku (Add and manage books)
- Registrasi anggota (Member registration)
- Peminjaman dan pengembalian buku (Issue and return books)
- Melihat riwayat buku yang pernah dipinjam (View issued book history)

Skema database
--------------
Database utama (contoh nama database: `library_system`) memiliki 3 tabel utama:

1) Books
- ID (INT, PRIMARY KEY, AUTO_INCREMENT)  
- Title (VARCHAR)  
- Author (VARCHAR)  
- ISBN (VARCHAR)  
- Availability (BOOLEAN / TINYINT(1))

2) Member
- ID (INT, PRIMARY KEY, AUTO_INCREMENT)  
- Name (VARCHAR)  
- Register_Date (DATE / DATETIME)

3) Issued_Books
- ID (INT, PRIMARY KEY, AUTO_INCREMENT)  
- Book_ID (INT) — foreign key ke Books(ID)  
- Member_ID (INT) — foreign key ke Member(ID)  
- Date_issued (DATE / DATETIME)  
- Date_returned (DATE / DATETIME, nullable)

Contoh SQL untuk membuat tabel (MySQL/MariaDB)
---------------------------------------------
```sql
CREATE DATABASE IF NOT EXISTS library_system;
USE library_system;

CREATE TABLE Books (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  Title VARCHAR(255) NOT NULL,
  Author VARCHAR(255),
  ISBN VARCHAR(100),
  Availability TINYINT(1) DEFAULT 1
);

CREATE TABLE Member (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  Name VARCHAR(255) NOT NULL,
  Register_Date DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Issued_Books (
  ID INT AUTO_INCREMENT PRIMARY KEY,
  Book_ID INT NOT NULL,
  Member_ID INT NOT NULL,
  Date_issued DATETIME DEFAULT CURRENT_TIMESTAMP,
  Date_returned DATETIME,
  FOREIGN KEY (Book_ID) REFERENCES Books(ID) ON DELETE CASCADE,
  FOREIGN KEY (Member_ID) REFERENCES Member(ID) ON DELETE CASCADE
);
