# Final Project for OOVP - Semester 20252

## Short Description

This repository contains the Final Project for the OOVP course (Semester 20252).

A simple CRUD (Create, Read, Update, Delete) application built using Java (OOP) with NetBeans as the primary IDE (can also be opened in VSCode). The app uses a local MySQL/MariaDB database (e.g., XAMPP, Laragon) — cloud options (e.g., Supabase) are also possible.

---

## Report / Documentation

Project report:  
https://docs.google.com/document/d/1N-WFUDSEtVoejvnkfw3rEKwMEA0iAOyLlUE9DmewL1Y/edit?usp=sharing

---

## Project Goal

Build a simple library management system with basic features:

- Add and manage books
- Member registration
- Issue and return books
- View issued book history

---

## Tech Stack

| Component | Technology |
|---|---|
| Language | Java (OOP) |
| IDE | NetBeans (primary), compatible with VSCode |
| Database | MySQL / MariaDB |
| Local Server | XAMPP / Laragon |
| JDBC Driver | MySQL Connector/J |
| Version Control | Git / GitHub |

Optional cloud database support:
- Supabase
- PostgreSQL  
(Requires JDBC driver and SQL adjustments)

---

## Prerequisites (Development)

Before running the project, make sure you have:

- Java JDK (recommended JDK 11+)
- NetBeans IDE  
  or VSCode with Java extensions
- MySQL / MariaDB
- XAMPP / Laragon (optional)
- MySQL Connector/J
- Git

---

## Local Setup (Short)

### 1. Create Database

Use phpMyAdmin or MySQL CLI to execute the SQL schema above.

### 2. Clone Repository

Clone the repository into your NetBeans projects folder.

### 3. Open Project

Open NetBeans:

```text
File → Open Project → Select Cloned Folder
```

### 4. Add JDBC Driver

Add MySQL Connector/J:

```text
Project Properties → Libraries → Add JAR/Folder
```

### 5. Configure Database Connection

Example JDBC URL:

```text
jdbc:mysql://localhost:3306/library_system
```

### 6. Run the Application

Build and run the project from NetBeans.

---

# Collaboration Guide

## Clone into NetBeans Default Folder and Use the `netbeans` Branch

To keep the `main` branch stable and avoid interfering with final deliverables, contributors should clone the repository into their NetBeans default projects folder and do work on the `netbeans` branch.

---

## Default NetBeans Project Folders

### Windows

```text
C:\Users\<User>\Documents\NetBeansProjects
```

### macOS

```text
/Users/<User>/NetBeansProjects
```

or

```text
~/NetBeansProjects
```

### Linux / Unix

```text
/home/<user>/NetBeansProjects
```

or

```text
~/NetBeansProjects
```

---

## Clone Repository into NetBeans Folder

### Windows (PowerShell)

```powershell
cd "$env:USERPROFILE\Documents\NetBeansProjects"
git clone https://github.com/Kklmfir/final-project-oovp2026.git
```

### Windows (CMD)

```cmd
cd %USERPROFILE%\Documents\NetBeansProjects
git clone https://github.com/Kklmfir/final-project-oovp2026.git
```

### macOS / Linux

```bash
cd ~/NetBeansProjects
git clone https://github.com/Kklmfir/final-project-oovp2026.git
```

---

## Create and Switch to `netbeans` Branch

After cloning:

```bash
cd final-project-oovp2026

# Create and switch to branch 'netbeans'
git checkout -b netbeans

# Push branch to remote and set upstream
git push -u origin netbeans
```

### If `netbeans` Already Exists on Remote

```bash
git fetch origin
git checkout netbeans
```

---

# Daily Workflow (Recommended)

## Before Starting Work

```bash
git checkout main
git pull origin main

git checkout netbeans

# Option A: Rebase
git rebase origin/main

# Option B: Merge
git merge origin/main
```

---

## Work on Features/Fixes

Commit frequently with meaningful commit messages.

Example:

```bash
git add .
git commit -m "feat: add book CRUD UI"
```

Push changes:

```bash
git push origin netbeans
```

---

# Pull Request Workflow

When changes are ready for production/final delivery:

1. Create a Pull Request from:
   - `netbeans`
   - or `feature/<name>`

2. Merge into:
   - `main`

3. Require:
   - Code review
   - Testing
   - Approval before merging

Keep `main` stable and production-ready.

---

# Resolving Conflicts and Syncing

If `main` has new commits:

```bash
git checkout netbeans

# Rebase
git rebase origin/main

# OR Merge
git merge origin/main
```

Resolve conflicts locally, test the project, then push changes.

If rebasing after pushing earlier commits:

```bash
git push --force-with-lease
```

Use carefully and coordinate with contributors.

---

# Recommended Branch Policy

| Branch | Purpose |
|---|---|
| `main` | Stable / final deliverables |
| `netbeans` | Ongoing integration & development |
| `feature/<name>` | Large isolated features |

---

# Recommended `.gitignore`

```gitignore
# NetBeans
nbproject/private/
build/
dist/

# Java
*.class
*.jar

# IDE
.idea/
*.iml

# OS
.DS_Store
Thumbs.db
```

---

# Notes and Tips

- Clone the repository into the NetBeans projects folder so NetBeans can recognize the project properly.
- VSCode users should ensure the project uses:
  - Ant
  - Maven
  - or Gradle
- If switching to PostgreSQL/Supabase:
  - update SQL schemas
  - use PostgreSQL JDBC drivers

---

# Contributing

- Work on the `netbeans` branch
- Push commits regularly
- Use feature branches for larger implementations
- Create Pull Requests only when features are tested and ready

---

# Contact

## Our Team

| Name | Email | GitHub |
|---|---|---|
| Alfairaz Putra Anantar | alfairaz.anantar@student.president.ac.id | [@AlfairazAnantar](https://github.com/AlfairazAnantar) |
| Azizah Putri Susanto | azizah.susanto@student.president.ac.id | [@*](https://github.com/) |
| Keefi Almer Firdaus | keefi.firdaus@student.president.ac.id | [@KeefiFirdaus](https://github.com/KeefiFirdaus) |
| M Fattah Fadli Rohman P | m.fattahputra@student.president.ac.id | [@*](https://github.com/) |
| Yesi Nurfitriyani | yesi.nurfitriyani@student.president.ac.id | [@*](https://github.com/) |

For questions, suggestions, or issues, please open an Issue in this repository.
