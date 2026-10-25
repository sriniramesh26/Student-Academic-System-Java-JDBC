-- ============================================================================
-- Kongu Engineering College (Autonomous), Perundurai, Erode - 638060
-- Regulation 2024 (R2024) - Student Academic & Continuous Evaluation System
-- Database Schema Script: kec_schema_sqlite.sql
-- RDBMS: SQLite 3.x
-- ============================================================================

DROP TABLE IF EXISTS assessment_marks;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- 1. Students Table
CREATE TABLE students (
    roll_no TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    department TEXT NOT NULL,
    semester INTEGER NOT NULL CHECK (semester >= 1 AND semester <= 8),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 2. Courses Table
CREATE TABLE courses (
    course_code TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    credits INTEGER NOT NULL CHECK (credits > 0 AND credits <= 10),
    course_type TEXT NOT NULL CHECK (course_type IN ('THEORY', 'PRACTICAL', 'EMBEDDED')),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 3. Enrollments Table
CREATE TABLE enrollments (
    enrollment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    roll_no TEXT NOT NULL,
    course_code TEXT NOT NULL,
    semester INTEGER NOT NULL CHECK (semester >= 1 AND semester <= 8),
    total_classes INTEGER NOT NULL DEFAULT 0 CHECK (total_classes >= 0),
    attended_classes INTEGER NOT NULL DEFAULT 0 CHECK (attended_classes >= 0),
    medical_condonation INTEGER NOT NULL DEFAULT 0 CHECK (medical_condonation IN (0, 1)),
    enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE,
    FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE,
    UNIQUE (roll_no, course_code, semester)
);

-- 4. Assessment Marks Table
CREATE TABLE assessment_marks (
    mark_id INTEGER PRIMARY KEY AUTOINCREMENT,
    enrollment_id INTEGER NOT NULL UNIQUE,
    cia_1 REAL DEFAULT NULL,
    cia_2 REAL DEFAULT NULL,
    assignment_marks REAL DEFAULT NULL,
    ese_marks REAL DEFAULT NULL,
    is_absent_ese INTEGER NOT NULL DEFAULT 0 CHECK (is_absent_ese IN (0, 1)),
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE
);
