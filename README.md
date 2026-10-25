# Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)
## Student Academic & Continuous Evaluation System using Java JDBC

An enterprise-grade, database-driven Java JDBC console application developed according to the official **Kongu Engineering College (Autonomous) - Regulation 2024 (R2024)** academic standards.

---

### Features & Capabilities

1. **Student & Course Management**:
   - Register students with Roll Number (e.g., `24ITR001`), Full Name, Department (CSE, IT, ECE, MECH, etc.), and Current Semester.
   - Course Catalog maintaining Course Code (`24ITT31`, `24ITP32`, `24ITE33`), Title, Credits, and Course Type (`THEORY`, `PRACTICAL`, or `EMBEDDED`).
   - Enrollment and continuous attendance tracking (total classes held vs. classes attended).

2. **Incremental Marks Entry ("Then and Now")**:
   - **Phase 1 (Mid-Semester)**: Record CIA Test 1 (0–100) and CIA Test 2 (0–100).
   - **Phase 2 (End of Semester - CIA)**: Record Assignments / Quizzes / Lab Evaluations (0–100).
   - **Phase 3 (Post Semester)**: Record End Semester Examination (ESE) Marks (0–100) and Absence (`AB`).

3. **KEC Regulation 2024 Evaluation Engine**:
   - **THEORY**: CIA Weightage = 40% | ESE Weightage = 60% | ESE Minimum = 45% (27/60) | Pass Minimum = 50 Marks
   - **PRACTICAL**: CIA Weightage = 60% | ESE Weightage = 40% | ESE Minimum = 50% (20/40) | Pass Minimum = 50 Marks
   - **EMBEDDED**: CIA Weightage = 50% | ESE Weightage = 50% | ESE Minimum = 45% (22.5/50) | Pass Minimum = 50 Marks

4. **Attendance Eligibility & Grading**:
   - $\ge 75\%$: Eligible for ESE.
   - $65\% \text{ to } 74.9\%$: Conditionally eligible (Medical Condonation approval required).
   - $< 65\%$: Awarded `SA` (Shortage of Attendance) grade (0 Grade Points, barred from ESE).
   - 10-Point Grading: `O` (91-100: 10), `A+` (81-90: 9), `A` (71-80: 8), `B+` (61-70: 7), `B` (50-60: 6), `RA` (Fail: 0), `SA` (0), `AB` (0).

5. **Formal Academic Reports**:
   - **Student Semester Mark Sheet**: Complete grade sheet showing internal scores, external scores, total marks, letter grade, grade points, SGPA, and Equivalent Percentage ($\text{SGPA} \times 10$).
   - **Attendance Defaulter Report**: Identifies students with $< 65\%$ attendance flagged with grade `SA`.
   - **Class Performance & Result Summary**: Department-wise pass/fail counts, pass rate percentage, and class average SGPA.

---

### Quick Start Guide

#### Option A: 1-Click Execution (Windows)
- Double-click `compile.bat` to compile all source files.
- Double-click `run.bat` to launch the interactive menu application.
- Double-click `test.bat` to run the automated verification suite.

#### Option B: Command Line (PowerShell / CMD / Bash)
```powershell
# 1. Compile
javac -encoding UTF-8 -cp ".;lib/*" *.java

# 2. Run Main Application
java -cp ".;lib/*" MainApp

# 3. Run Automated Tests
java -cp ".;lib/*" VerifyKecEngine
```

---

### Database Setup & Configuration
By default, the application runs on SQLite (`kec_academic_r2024.db`) with zero manual setup. All required drivers are bundled in `lib/`.

To switch to **MySQL Server**:
1. Execute `kec_schema.sql` in MySQL Workbench or CLI.
2. Edit `db.properties`:
   ```properties
   db.type=mysql
   mysql.url=jdbc:mysql://localhost:3306/kec_academic_r2024?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
   mysql.user=root
   mysql.password=your_password
   ```

---

### Execution Screenshots
All screenshot proofs are located in [`docs/screenshots/`](file:///c:/Users/srini/Downloads/project-bolt-sb1-fszx1w13/docs/screenshots/):
- `screenshot_1_add_student_course.png`: Proof of student and course data entry.
- `screenshot_2_incremental_marks.png`: Proof of phased marks entry (Phase 1, 2, 3).
- `screenshot_3_grade_sheet_sgpa.png`: Proof of generated Semester Mark Sheet with calculated SGPA.
- `screenshot_4_attendance_defaulters.png`: Proof of Attendance Defaulter Report.
- `screenshot_5_class_summary.png`: Proof of Class Performance & Result Summary.
