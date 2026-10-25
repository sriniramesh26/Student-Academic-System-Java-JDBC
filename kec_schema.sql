-- ============================================================================
-- Kongu Engineering College (Autonomous), Perundurai, Erode - 638060
-- Regulation 2024 (R2024) - Student Academic & Continuous Evaluation System
-- Database Schema Script: kec_schema.sql
-- RDBMS: MySQL 8.x / MariaDB
-- ============================================================================

CREATE DATABASE IF NOT EXISTS kec_academic_r2024;
USE kec_academic_r2024;

-- Drop tables if already existing (in reverse dependency order)
DROP TABLE IF EXISTS assessment_marks;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;

-- ----------------------------------------------------------------------------
-- 1. Students Table
-- Stores student demographic and registration details
-- ----------------------------------------------------------------------------
CREATE TABLE students (
    roll_no VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(20) NOT NULL,
    semester INT NOT NULL CHECK (semester >= 1 AND semester <= 8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 2. Courses Table
-- Stores course catalog with course type and credit allocations
-- Course Types: THEORY, PRACTICAL, EMBEDDED
-- ----------------------------------------------------------------------------
CREATE TABLE courses (
    course_code VARCHAR(20) PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    credits INT NOT NULL CHECK (credits > 0 AND credits <= 10),
    course_type VARCHAR(20) NOT NULL CHECK (course_type IN ('THEORY', 'PRACTICAL', 'EMBEDDED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. Course Enrollments & Attendance Tracking Table
-- Records course enrollments per semester and continuous attendance monitoring
-- ----------------------------------------------------------------------------
CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(20) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL CHECK (semester >= 1 AND semester <= 8),
    total_classes INT NOT NULL DEFAULT 0 CHECK (total_classes >= 0),
    attended_classes INT NOT NULL DEFAULT 0 CHECK (attended_classes >= 0 AND attended_classes <= total_classes),
    medical_condonation INT NOT NULL DEFAULT 0 CHECK (medical_condonation IN (0, 1)),
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT uq_student_course_sem UNIQUE (roll_no, course_code, semester)
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 4. Assessment Marks Table (Phased Incremental Evaluation)
-- Supports Phase 1 (CIA1 & CIA2), Phase 2 (Assignments/Quizzes/Lab), Phase 3 (ESE)
-- ----------------------------------------------------------------------------
CREATE TABLE assessment_marks (
    mark_id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT NOT NULL UNIQUE,
    cia_1 DECIMAL(5,2) DEFAULT NULL CHECK (cia_1 IS NULL OR (cia_1 >= 0 AND cia_1 <= 100)),
    cia_2 DECIMAL(5,2) DEFAULT NULL CHECK (cia_2 IS NULL OR (cia_2 >= 0 AND cia_2 <= 100)),
    assignment_marks DECIMAL(5,2) DEFAULT NULL CHECK (assignment_marks IS NULL OR (assignment_marks >= 0 AND assignment_marks <= 100)),
    ese_marks DECIMAL(5,2) DEFAULT NULL CHECK (ese_marks IS NULL OR (ese_marks >= 0 AND ese_marks <= 100)),
    is_absent_ese INT NOT NULL DEFAULT 0 CHECK (is_absent_ese IN (0, 1)),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_marks_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- Sample Initial Data for Verification & Demonstration
-- ----------------------------------------------------------------------------

-- Insert Students
INSERT INTO students (roll_no, name, department, semester) VALUES
('24ITR001', 'Aravind Kumar S', 'IT', 3),
('24ITR002', 'Bhavani Devi M', 'IT', 3),
('24ITR003', 'Chandru P', 'IT', 3),
('24CSR015', 'Deepak Raja V', 'CSE', 3),
('24ECR042', 'Elango K', 'ECE', 3);

-- Insert Courses (Theory, Practical, Embedded)
INSERT INTO courses (course_code, title, credits, course_type) VALUES
('24ITT31', 'Database Management Systems', 3, 'THEORY'),
('24ITP32', 'Data Structures & Algorithms Laboratory', 2, 'PRACTICAL'),
('24ITE33', 'Object-Oriented Programming with Java', 4, 'EMBEDDED'),
('24CST31', 'Computer Organization & Architecture', 3, 'THEORY');

-- Insert Enrollments with Varying Attendance
-- 24ITR001: Good attendance (>75%) in all
INSERT INTO enrollments (enrollment_id, roll_no, course_code, semester, total_classes, attended_classes, medical_condonation) VALUES
(1, '24ITR001', '24ITT31', 3, 50, 46, 0), -- 92%
(2, '24ITR001', '24ITP32', 3, 30, 28, 0), -- 93.3%
(3, '24ITR001', '24ITE33', 3, 60, 55, 0); -- 91.7%

-- 24ITR002: Condonation case (65% - 74.9%)
INSERT INTO enrollments (enrollment_id, roll_no, course_code, semester, total_classes, attended_classes, medical_condonation) VALUES
(4, '24ITR002', '24ITT31', 3, 50, 36, 1), -- 72% with condonation approved
(5, '24ITR002', '24ITP32', 3, 30, 27, 0), -- 90%
(6, '24ITR002', '24ITE33', 3, 60, 44, 1); -- 73.3% with condonation approved

-- 24ITR003: Attendance Defaulter (<65% -> SA Grade)
INSERT INTO enrollments (enrollment_id, roll_no, course_code, semester, total_classes, attended_classes, medical_condonation) VALUES
(7, '24ITR003', '24ITT31', 3, 50, 26, 0), -- 52% (SA)
(8, '24ITR003', '24ITP32', 3, 30, 25, 0), -- 83.3%
(9, '24ITR003', '24ITE33', 3, 60, 32, 0); -- 53.3% (SA)

-- Insert Assessment Marks across Phases
-- 24ITR001: High Achiever (O / A+ grades)
INSERT INTO assessment_marks (enrollment_id, cia_1, cia_2, assignment_marks, ese_marks, is_absent_ese) VALUES
(1, 94.0, 96.0, 92.0, 95.0, 0), -- Theory
(2, 98.0, 95.0, 96.0, 92.0, 0), -- Practical
(3, 90.0, 92.0, 95.0, 88.0, 0); -- Embedded

-- 24ITR002: Moderate marks
INSERT INTO assessment_marks (enrollment_id, cia_1, cia_2, assignment_marks, ese_marks, is_absent_ese) VALUES
(4, 75.0, 78.0, 80.0, 72.0, 0), -- Theory
(5, 82.0, 80.0, 85.0, 78.0, 0), -- Practical
(6, 68.0, 70.0, 75.0, 65.0, 0); -- Embedded

-- 24ITR003: Defaulter / Fail / Absent
INSERT INTO assessment_marks (enrollment_id, cia_1, cia_2, assignment_marks, ese_marks, is_absent_ese) VALUES
(7, 45.0, 50.0, 60.0, NULL, 0), -- Barred due to SA
(8, 70.0, 72.0, 80.0, 35.0, 0), -- Failed ESE minimum threshold (35/100 -> 14/40 < 20) -> RA
(9, 40.0, 42.0, 50.0, NULL, 1); -- SA / AB
