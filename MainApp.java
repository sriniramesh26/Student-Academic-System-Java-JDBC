import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Kongu Engineering College (Autonomous), Perundurai, Erode - 638060
 * Regulation 2024 (R2024) - Student Academic & Continuous Evaluation System
 *
 * MainApp: Menu-Driven Interactive Interface for JDBC Data Entry & Reporting.
 */
public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final AcademicDAO academicDAO = new AcademicDAO();

    public static void main(String[] args) {
        printHeader();

        // Ensure database connection and initial tables
        try {
            DBConnection.getConnection();
            System.out.println("[INFO] Connected successfully to database engine: " + DBConnection.getDatabaseType().toUpperCase());
        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select an option (1-5): ", 1, 5);
            System.out.println();

            switch (choice) {
                case 1:
                    handleStudentAndCourseManagement();
                    break;
                case 2:
                    handleIncrementalMarksEntry();
                    break;
                case 3:
                    handleAcademicEvaluationAndReports();
                    break;
                case 4:
                    handleDatabaseAndUtilities();
                    break;
                case 5:
                    System.out.println("=========================================================================================");
                    System.out.println("   Thank you for using KEC R2024 Evaluation System. Exiting application. Goodbye!   ");
                    System.out.println("=========================================================================================");
                    running = false;
                    break;
            }
            if (running) {
                pressEnterToContinue();
            }
        }
    }

    private static void printHeader() {
        System.out.println("=========================================================================================");
        System.out.println("               KONGU ENGINEERING COLLEGE (AUTONOMOUS) - ERODE 638060                     ");
        System.out.println("       AFFILIATED TO ANNA UNIVERSITY | ACCREDITED BY NAAC WITH 'A++' GRADE               ");
        System.out.println("            REGULATION 2024 (R2024) CONTINUOUS EVALUATION SYSTEM (JDBC)                  ");
        System.out.println("=========================================================================================");
    }

    private static void printMainMenu() {
        System.out.println("\n-----------------------------------------------------------------------------------------");
        System.out.println("                                      MAIN MENU                                          ");
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("  [1] Student & Course Management (Registration, Catalog, Enrollments, Attendance)       ");
        System.out.println("  [2] Incremental Marks Entry ('Then and Now' - Phase 1, Phase 2, Phase 3)               ");
        System.out.println("  [3] KEC R2024 Academic Evaluation & Reports (Mark Sheet, Defaulters, Class Summary)    ");
        System.out.println("  [4] Database Utilities & Sample Demonstration Data                                     ");
        System.out.println("  [5] Exit Application                                                                   ");
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    // =========================================================================
    // 1. STUDENT & COURSE MANAGEMENT MENU
    // =========================================================================

    private static void handleStudentAndCourseManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-----------------------------------------------------------------------------------------");
            System.out.println("                        1. STUDENT & COURSE MANAGEMENT                                  ");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("  [1] Register New Student (Roll Number, Name, Department, Semester)");
            System.out.println("  [2] Add Course to Catalog (Course Code, Title, Credits, Course Type)");
            System.out.println("  [3] Enroll Student in Course & Record Attendance");
            System.out.println("  [4] Update Attendance Tracking (Total Classes vs Attended Classes)");
            System.out.println("  [5] Manage Medical Condonation Approval (65% to 74.9%)");
            System.out.println("  [6] View All Registered Students");
            System.out.println("  [7] View All Course Catalog");
            System.out.println("  [8] View All Course Enrollments");
            System.out.println("  [9] Return to Main Menu");
            System.out.println("-----------------------------------------------------------------------------------------");

            int choice = readInt("Select option (1-9): ", 1, 9);
            System.out.println();

            try {
                switch (choice) {
                    case 1:
                        registerStudentUI();
                        break;
                    case 2:
                        addCourseUI();
                        break;
                    case 3:
                        enrollStudentUI();
                        break;
                    case 4:
                        updateAttendanceUI();
                        break;
                    case 5:
                        manageCondonationUI();
                        break;
                    case 6:
                        listStudentsUI();
                        break;
                    case 7:
                        listCoursesUI();
                        break;
                    case 8:
                        listEnrollmentsUI();
                        break;
                    case 9:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Operation failed: " + e.getMessage());
            }
        }
    }

    private static void registerStudentUI() throws SQLException {
        System.out.println(">>> 1.1 Register New Student");
        String rollNo = readString("Enter Roll Number (e.g. 24ITR001): ").toUpperCase();
        if (studentDAO.getStudentByRollNo(rollNo) != null) {
            System.out.println("[WARNING] Student with Roll Number " + rollNo + " already exists!");
            return;
        }
        String name = readString("Enter Student Full Name: ");
        String dept = readString("Enter Department (CSE, IT, ECE, MECH, etc.): ").toUpperCase();
        int sem = readInt("Enter Current Semester (1-8): ", 1, 8);

        Student s = new Student(rollNo, name, dept, sem);
        if (studentDAO.addStudent(s)) {
            System.out.println("[SUCCESS] Student registered successfully:");
            System.out.println("          " + s);
        } else {
            System.out.println("[FAILURE] Could not register student.");
        }
    }

    private static void addCourseUI() throws SQLException {
        System.out.println(">>> 1.2 Add Course to Catalog");
        String code = readString("Enter Course Code (e.g. 24ITT31, 24ITP32, 24ITE33): ").toUpperCase();
        if (academicDAO.getCourseByCode(code) != null) {
            System.out.println("[WARNING] Course with code " + code + " already exists!");
            return;
        }
        String title = readString("Enter Course Title: ");
        int credits = readInt("Enter Credits (1-8): ", 1, 8);

        System.out.println("Select Course Type according to KEC R2024:");
        System.out.println("  1. THEORY    (CIA: 40% | ESE: 60% | ESE Min: 27/60)");
        System.out.println("  2. PRACTICAL (CIA: 60% | ESE: 40% | ESE Min: 20/40)");
        System.out.println("  3. EMBEDDED  (CIA: 50% | ESE: 50% | ESE Min: 22.5/50)");
        int typeChoice = readInt("Enter choice (1-3): ", 1, 3);
        String courseType = (typeChoice == 1) ? "THEORY" : (typeChoice == 2) ? "PRACTICAL" : "EMBEDDED";

        Course course = new Course(code, title, credits, courseType);
        if (academicDAO.addCourse(course)) {
            System.out.println("[SUCCESS] Course added to catalog successfully:");
            System.out.println("          " + course);
        } else {
            System.out.println("[FAILURE] Could not add course.");
        }
    }

    private static void enrollStudentUI() throws SQLException {
        System.out.println(">>> 1.3 Course Enrollment & Attendance Tracking");
        String rollNo = readString("Enter Student Roll Number: ").toUpperCase();
        Student student = studentDAO.getStudentByRollNo(rollNo);
        if (student == null) {
            System.out.println("[ERROR] Student " + rollNo + " not found. Please register first.");
            return;
        }

        String courseCode = readString("Enter Course Code: ").toUpperCase();
        Course course = academicDAO.getCourseByCode(courseCode);
        if (course == null) {
            System.out.println("[ERROR] Course " + courseCode + " not found in catalog.");
            return;
        }

        int sem = readInt("Enter Semester (Default " + student.getSemester() + "): ", 1, 8);
        if (academicDAO.getEnrollment(rollNo, courseCode, sem) != null) {
            System.out.println("[WARNING] Student is already enrolled in " + courseCode + " for Semester " + sem);
            return;
        }

        int total = readInt("Enter Total Classes Held (e.g. 50): ", 0, 200);
        int attended = readInt("Enter Classes Attended by Student (0 - " + total + "): ", 0, total);
        double attPct = (total > 0) ? (attended * 100.0 / total) : 100.0;
        System.out.printf("Computed Attendance: %.2f%%\n", attPct);

        boolean condonation = false;
        if (attPct >= 65.0 && attPct < 75.0) {
            System.out.println("[NOTICE] Attendance is between 65% and 74.9%. Conditionally eligible with Medical Condonation.");
            String ans = readString("Approve Medical Condonation now? (Y/N): ").toUpperCase();
            condonation = ans.startsWith("Y");
        } else if (attPct < 65.0) {
            System.out.println("[ALERT] Attendance is < 65%. Student will be awarded 'SA' (Shortage of Attendance) and barred from ESE.");
        }

        int id = academicDAO.enrollStudent(rollNo, courseCode, sem, total, attended, condonation);
        if (id > 0) {
            System.out.println("[SUCCESS] Student successfully enrolled (Enrollment ID: " + id + ")");
        } else {
            System.out.println("[FAILURE] Enrollment could not be created.");
        }
    }

    private static void updateAttendanceUI() throws SQLException {
        System.out.println(">>> 1.4 Update Attendance Tracking");
        String rollNo = readString("Enter Student Roll Number: ").toUpperCase();
        int sem = readInt("Enter Semester (1-8): ", 1, 8);
        List<Enrollment> enrollments = academicDAO.getEnrollmentsByStudent(rollNo, sem);
        if (enrollments.isEmpty()) {
            System.out.println("[INFO] No course enrollments found for student " + rollNo + " in Semester " + sem);
            return;
        }

        printEnrollmentTable(enrollments);
        int enrollId = readInt("Enter Enrollment ID to update attendance: ", 1, Integer.MAX_VALUE);
        Enrollment target = academicDAO.getEnrollmentById(enrollId);
        if (target == null) {
            System.out.println("[ERROR] Invalid Enrollment ID.");
            return;
        }

        int total = readInt("Enter New Total Classes Held: ", 0, 200);
        int attended = readInt("Enter New Classes Attended: ", 0, total);
        if (academicDAO.updateAttendance(enrollId, total, attended)) {
            double pct = (total > 0) ? (attended * 100.0 / total) : 100.0;
            System.out.printf("[SUCCESS] Attendance updated successfully. New Attendance: %.2f%%\n", pct);
            if (pct < 65.0) {
                System.out.println("[STATUS] Barred from ESE (Grade: SA)");
            } else if (pct < 75.0) {
                System.out.println("[STATUS] Conditionally Eligible (Requires Medical Condonation Approval)");
            } else {
                System.out.println("[STATUS] Eligible for ESE");
            }
        } else {
            System.out.println("[FAILURE] Could not update attendance.");
        }
    }

    private static void manageCondonationUI() throws SQLException {
        System.out.println(">>> 1.5 Medical Condonation Approval (65% to 74.9%)");
        String rollNo = readString("Enter Student Roll Number: ").toUpperCase();
        int sem = readInt("Enter Semester: ", 1, 8);
        List<Enrollment> enrollments = academicDAO.getEnrollmentsByStudent(rollNo, sem);
        if (enrollments.isEmpty()) {
            System.out.println("[INFO] No enrollments found.");
            return;
        }

        printEnrollmentTable(enrollments);
        int enrollId = readInt("Enter Enrollment ID for Condonation Approval/Revocation: ", 1, Integer.MAX_VALUE);
        Enrollment enroll = academicDAO.getEnrollmentById(enrollId);
        if (enroll == null) {
            System.out.println("[ERROR] Enrollment not found.");
            return;
        }

        double pct = enroll.getAttendancePercentage();
        System.out.printf("Selected Course: %s | Attendance: %.2f%% | Current Condonation: %s\n",
                enroll.getCourseCode(), pct, enroll.isMedicalCondonation() ? "APPROVED" : "NOT APPROVED");

        if (pct < 65.0) {
            System.out.println("[WARNING] Attendance is below 65%. Per KEC R2024 regulations, condonation cannot be granted below 65% (Mandatory SA).");
        } else if (pct >= 75.0) {
            System.out.println("[INFO] Attendance is >= 75%. Student is already fully eligible without condonation.");
        }

        String ans = readString("Set Medical Condonation to APPROVED? (Y/N): ").toUpperCase();
        boolean approved = ans.startsWith("Y");
        if (academicDAO.setMedicalCondonation(enrollId, approved)) {
            System.out.println("[SUCCESS] Medical condonation status updated to: " + (approved ? "APPROVED" : "NOT APPROVED"));
        } else {
            System.out.println("[FAILURE] Could not update condonation status.");
        }
    }

    private static void listStudentsUI() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        System.out.println("\n=========================================================================================");
        System.out.println("                              REGISTERED STUDENTS DIRECTORY                              ");
        System.out.println("=========================================================================================");
        if (students.isEmpty()) {
            System.out.println("No students registered in the system.");
            return;
        }
        System.out.printf("%-12s | %-30s | %-12s | %-10s\n", "ROLL NO", "STUDENT NAME", "DEPARTMENT", "SEMESTER");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Student s : students) {
            System.out.printf("%-12s | %-30s | %-12s | Semester %-2d\n",
                    s.getRollNo(), s.getName(), s.getDepartment(), s.getSemester());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Total Students: " + students.size());
    }

    private static void listCoursesUI() throws SQLException {
        List<Course> courses = academicDAO.getAllCourses();
        System.out.println("\n=========================================================================================");
        System.out.println("                                  COURSE CATALOG (KEC R2024)                             ");
        System.out.println("=========================================================================================");
        if (courses.isEmpty()) {
            System.out.println("No courses registered in catalog.");
            return;
        }
        System.out.printf("%-12s | %-40s | %-8s | %-12s\n", "COURSE CODE", "COURSE TITLE", "CREDITS", "COURSE TYPE");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Course c : courses) {
            System.out.printf("%-12s | %-40s | %-8d | %-12s\n",
                    c.getCourseCode(), c.getTitle(), c.getCredits(), c.getCourseType());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("Total Courses: " + courses.size());
    }

    private static void listEnrollmentsUI() throws SQLException {
        List<Enrollment> enrollments = academicDAO.getAllEnrollments();
        System.out.println("\n=========================================================================================");
        System.out.println("                               ALL COURSE ENROLLMENTS                                    ");
        System.out.println("=========================================================================================");
        printEnrollmentTable(enrollments);
    }

    private static void printEnrollmentTable(List<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }
        System.out.printf("%-4s | %-10s | %-20s | %-10s | %-8s | %-10s | %-12s | %-12s\n",
                "ID", "ROLL NO", "STUDENT NAME", "COURSE", "CREDITS", "ATTENDED", "ATTENDANCE %", "CONDONATION");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Enrollment e : enrollments) {
            double pct = e.getAttendancePercentage();
            String cond = e.isMedicalCondonation() ? "APPROVED" : "NO";
            System.out.printf("%-4d | %-10s | %-20s | %-10s | %-8d | %3d / %-3d  | %6.1f%%      | %-12s\n",
                    e.getEnrollmentId(), e.getRollNo(),
                    truncate(e.getStudentName(), 20),
                    e.getCourseCode(), e.getCredits(),
                    e.getAttendedClasses(), e.getTotalClasses(),
                    pct, cond);
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }

    // =========================================================================
    // 2. INCREMENTAL MARKS ENTRY ("THEN AND NOW")
    // =========================================================================

    private static void handleIncrementalMarksEntry() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-----------------------------------------------------------------------------------------");
            System.out.println("              2. INCREMENTAL MARKS ENTRY ('THEN AND NOW' PHASES)                         ");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("  [1] Phase 1 (Mid-Semester)    : Record CIA Test 1 (0-100) & CIA Test 2 (0-100)");
            System.out.println("  [2] Phase 2 (End-of-Semester) : Record Assignments / Quizzes / Lab Evaluations (0-100)");
            System.out.println("  [3] Phase 3 (Post-Semester)   : Record End Semester Exam (ESE) Marks & Mark Absence");
            System.out.println("  [4] View Current Marks Status for a Student");
            System.out.println("  [5] Return to Main Menu");
            System.out.println("-----------------------------------------------------------------------------------------");

            int choice = readInt("Select option (1-5): ", 1, 5);
            System.out.println();

            try {
                switch (choice) {
                    case 1:
                        recordPhase1UI();
                        break;
                    case 2:
                        recordPhase2UI();
                        break;
                    case 3:
                        recordPhase3UI();
                        break;
                    case 4:
                        viewStudentMarksStatusUI();
                        break;
                    case 5:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Marks entry failed: " + e.getMessage());
            }
        }
    }

    private static void recordPhase1UI() throws SQLException {
        System.out.println(">>> 2.1 Phase 1 (Mid-Semester): CIA Test 1 & CIA Test 2");
        Enrollment enroll = selectEnrollmentPrompt();
        if (enroll == null) return;

        System.out.printf("Recording Phase 1 Marks for: %s | Course: %s (%s)\n",
                enroll.getRollNo(), enroll.getCourseCode(), enroll.getCourseType());

        double cia1 = readDouble("Enter CIA Test 1 Marks (0.0 - 100.0): ", 0.0, 100.0);
        double cia2 = readDouble("Enter CIA Test 2 Marks (0.0 - 100.0): ", 0.0, 100.0);

        if (academicDAO.recordPhase1Marks(enroll.getEnrollmentId(), cia1, cia2)) {
            System.out.printf("[SUCCESS] Phase 1 Recorded: CIA 1 = %.2f, CIA 2 = %.2f (Test Average: %.2f/100)\n",
                    cia1, cia2, (cia1 + cia2) / 2.0);
        } else {
            System.out.println("[FAILURE] Could not record Phase 1 marks.");
        }
    }

    private static void recordPhase2UI() throws SQLException {
        System.out.println(">>> 2.2 Phase 2 (End-of-Semester CIA): Assignments / Quizzes / Lab Evaluations");
        Enrollment enroll = selectEnrollmentPrompt();
        if (enroll == null) return;

        System.out.printf("Recording Phase 2 Marks for: %s | Course: %s (%s)\n",
                enroll.getRollNo(), enroll.getCourseCode(), enroll.getCourseType());

        String componentName = "PRACTICAL".equalsIgnoreCase(enroll.getCourseType()) ?
                "Lab Model/Continuous Practical Evaluation" : "Assignments / Quizzes / Tutorials";
        double assign = readDouble("Enter " + componentName + " Marks (0.0 - 100.0): ", 0.0, 100.0);

        if (academicDAO.recordPhase2Marks(enroll.getEnrollmentId(), assign)) {
            System.out.printf("[SUCCESS] Phase 2 Recorded: %s = %.2f/100\n", componentName, assign);
        } else {
            System.out.println("[FAILURE] Could not record Phase 2 marks.");
        }
    }

    private static void recordPhase3UI() throws SQLException {
        System.out.println(">>> 2.3 Phase 3 (Post-Semester): End Semester Examination (ESE) Marks & Absence");
        Enrollment enroll = selectEnrollmentPrompt();
        if (enroll == null) return;

        Course course = academicDAO.getCourseByCode(enroll.getCourseCode());
        AssessmentMarks currentMarks = academicDAO.getMarksByEnrollmentId(enroll.getEnrollmentId());
        CourseEvaluationResult eval = academicDAO.evaluateCourse(enroll, currentMarks, course);

        System.out.printf("Student: %s | Course: %s (%s) | Attendance: %.2f%% (%s)\n",
                enroll.getRollNo(), enroll.getCourseCode(), course.getCourseType(),
                eval.getAttendancePercentage(), eval.getAttendanceStatus());

        if (eval.isBarred()) {
            System.out.println("*****************************************************************************************");
            System.out.println("[BARRED FROM ESE] Student has attendance shortage (< 65% or unapproved condonation)!");
            System.out.println("Per KEC R2024 regulations, student is NOT eligible to write End Semester Examination.");
            System.out.println("System will award Grade: 'SA' (Shortage of Attendance) with 0 Grade Points.");
            System.out.println("*****************************************************************************************");
            return;
        }

        String absInput = readString("Was the student ABSENT in ESE? (Y/N): ").toUpperCase();
        boolean isAbsent = absInput.startsWith("Y");

        Double eseMarks = null;
        if (!isAbsent) {
            eseMarks = readDouble("Enter End Semester Examination (ESE) Marks (0.0 - 100.0): ", 0.0, 100.0);
        }

        if (academicDAO.recordPhase3Marks(enroll.getEnrollmentId(), eseMarks, isAbsent)) {
            if (isAbsent) {
                System.out.println("[SUCCESS] Student marked ABSENT in ESE. Final Grade: 'AB' (0 Grade Points).");
            } else {
                double weight = eval.getEseWeightage();
                double scaled = (eseMarks * weight) / 100.0;
                System.out.printf("[SUCCESS] ESE Marks Recorded: %.2f/100. Scaled ESE (%.0f%% weightage): %.2f/%.0f\n",
                        eseMarks, weight, scaled, weight);
                System.out.printf("          ESE Minimum Pass Threshold: %.1f marks (Met: %s)\n",
                        eval.getEseMinThreshold(), (scaled >= eval.getEseMinThreshold() ? "YES" : "NO -> Grade RA"));
            }
        } else {
            System.out.println("[FAILURE] Could not record Phase 3 marks.");
        }
    }

    private static void viewStudentMarksStatusUI() throws SQLException {
        System.out.println(">>> 2.4 View Student Current Marks Status");
        String rollNo = readString("Enter Student Roll Number: ").toUpperCase();
        int sem = readInt("Enter Semester: ", 1, 8);
        List<Enrollment> enrollments = academicDAO.getEnrollmentsByStudent(rollNo, sem);
        if (enrollments.isEmpty()) {
            System.out.println("[INFO] No enrollments found for " + rollNo + " in Semester " + sem);
            return;
        }

        System.out.println("\n==================================================================================================================");
        System.out.printf("                           INCREMENTAL MARKS STATUS: %s (SEMESTER %d)                                      \n", rollNo, sem);
        System.out.println("==================================================================================================================");
        System.out.printf("%-10s | %-8s | %-10s | %-8s | %-8s | %-12s | %-10s | %-8s\n",
                "COURSE", "TYPE", "ATTEND %", "CIA 1", "CIA 2", "ASSIGN/LAB", "ESE (100)", "ESE STAT");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        for (Enrollment e : enrollments) {
            AssessmentMarks m = academicDAO.getMarksByEnrollmentId(e.getEnrollmentId());
            String cia1Str = (m != null && m.getCia1() != null) ? String.format("%.1f", m.getCia1()) : "-";
            String cia2Str = (m != null && m.getCia2() != null) ? String.format("%.1f", m.getCia2()) : "-";
            String assignStr = (m != null && m.getAssignmentMarks() != null) ? String.format("%.1f", m.getAssignmentMarks()) : "-";
            String eseStr = (m != null && m.getEseMarks() != null) ? String.format("%.1f", m.getEseMarks()) : "-";
            String eseStat = (m != null && m.isAbsentEse()) ? "ABSENT" : ((m != null && m.getEseMarks() != null) ? "APPEARED" : "PENDING");

            System.out.printf("%-10s | %-8s | %6.1f%%   | %-8s | %-8s | %-12s | %-10s | %-8s\n",
                    e.getCourseCode(), e.getCourseType(), e.getAttendancePercentage(),
                    cia1Str, cia2Str, assignStr, eseStr, eseStat);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }

    private static Enrollment selectEnrollmentPrompt() throws SQLException {
        String rollNo = readString("Enter Student Roll Number: ").toUpperCase();
        int sem = readInt("Enter Semester: ", 1, 8);
        List<Enrollment> enrollments = academicDAO.getEnrollmentsByStudent(rollNo, sem);
        if (enrollments.isEmpty()) {
            System.out.println("[INFO] No enrollments found for student " + rollNo + " in Semester " + sem);
            return null;
        }

        printEnrollmentTable(enrollments);
        int enrollId = readInt("Enter Enrollment ID to record marks: ", 1, Integer.MAX_VALUE);
        Enrollment enroll = academicDAO.getEnrollmentById(enrollId);
        if (enroll == null) {
            System.out.println("[ERROR] Invalid Enrollment ID selected.");
            return null;
        }
        return enroll;
    }

    // =========================================================================
    // 3. KEC R2024 EVALUATION & REPORTS
    // =========================================================================

    private static void handleAcademicEvaluationAndReports() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-----------------------------------------------------------------------------------------");
            System.out.println("              3. KEC R2024 ACADEMIC EVALUATION & REPORTS                                 ");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("  [1] Student Semester Mark Sheet (Formal Grade Sheet with SGPA & Percentage)");
            System.out.println("  [2] Attendance Defaulter Report (Attendance < 65% Flagged with Grade SA)");
            System.out.println("  [3] Class Performance & Result Summary (Department Pass/Fail Count & Avg SGPA)");
            System.out.println("  [4] Return to Main Menu");
            System.out.println("-----------------------------------------------------------------------------------------");

            int choice = readInt("Select option (1-4): ", 1, 4);
            System.out.println();

            try {
                switch (choice) {
                    case 1:
                        generateMarkSheetUI();
                        break;
                    case 2:
                        generateAttendanceDefaulterReportUI();
                        break;
                    case 3:
                        generateClassSummaryUI();
                        break;
                    case 4:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Report generation failed: " + e.getMessage());
            }
        }
    }

    public static void generateMarkSheetUI() throws SQLException {
        System.out.println(">>> 3.1 Student Semester Mark Sheet");
        String rollNo = readString("Enter Student Roll Number (e.g. 24ITR001): ").toUpperCase();
        int sem = readInt("Enter Semester (1-8): ", 1, 8);

        StudentSemesterResult report = academicDAO.generateStudentSemesterReport(rollNo, sem);
        if (report == null || report.getCourseResults().isEmpty()) {
            System.out.println("[INFO] No records found for Roll Number: " + rollNo + " in Semester: " + sem);
            return;
        }

        printStudentMarkSheet(report);
    }

    public static void printStudentMarkSheet(StudentSemesterResult report) {
        Student s = report.getStudent();
        System.out.println();
        System.out.println("========================================================================================================================");
        System.out.println("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                     ");
        System.out.println("                                    PERUNDURAI, ERODE - 638060                                          ");
        System.out.println("                                OFFICE OF THE CONTROLLER OF EXAMINATIONS                                ");
        System.out.println("                          GRADE SHEET - REGULATION 2024 (AUTONOMOUS)                                    ");
        System.out.println("========================================================================================================================");
        System.out.printf("  Roll Number : %-20s  Student Name : %-35s\n", s.getRollNo(), s.getName());
        System.out.printf("  Department  : %-20s  Semester     : %-35s\n", s.getDepartment(), "Semester " + report.getSemester());
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-8s | %-28s | %-4s | %-6s | %-8s | %-8s | %-5s | %-5s | %-6s | %-6s | %-12s\n",
                "CODE", "COURSE TITLE", "TYP", "CRED", "ATTEND%", "CIA(SCL)", "ESE", "TOT", "GRADE", "POINTS", "STATUS");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        for (CourseEvaluationResult cr : report.getCourseResults()) {
            String eseDisplay = cr.isAbsent() ? "AB" : (cr.isBarred() ? "SA" : String.format("%.0f", cr.getEseScaled()));
            String typeShort = cr.getCourseType().substring(0, Math.min(3, cr.getCourseType().length()));

            System.out.printf("%-8s | %-28s | %-4s | %-6d | %5.1f%%  | %6.1f   | %-5s | %-5.0f | %-6s | %-6d | %-12s\n",
                    cr.getCourseCode(),
                    truncate(cr.getCourseTitle(), 28),
                    typeShort,
                    cr.getCredits(),
                    cr.getAttendancePercentage(),
                    cr.getCiaScaled(),
                    eseDisplay,
                    cr.getTotalMarks(),
                    cr.getLetterGrade(),
                    cr.getGradePoints(),
                    cr.getResultStatus());
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("  Total Credits Enrolled : %-6d   Total Credits Earned : %-6d   Total Credit Points : %d\n",
                report.getTotalCreditsEnrolled(), report.getTotalCreditsEarned(), report.getTotalCreditPoints());
        System.out.printf("  SEMESTER GPA (SGPA)    : %-6.2f   EQUIVALENT PERCENTAGE: %.2f%%\n",
                report.getSgpa(), report.getEquivalentPercentage());
        System.out.printf("  OVERALL SEMESTER STATUS: %s\n",
                report.isPassedSemester() ? "PASSED ALL COURSES" : "RE-APPEARANCE / ARREARS REQUIRED");
        System.out.println("========================================================================================================================");
        System.out.println("  Grading Scale (KEC R2024):                                                                                            ");
        System.out.println("  O (91-100: 10) | A+ (81-90: 9) | A (71-80: 8) | B+ (61-70: 7) | B (50-60: 6)                                          ");
        System.out.println("  RA: Re-Appearance (<50 or ESE Min Failed) | SA: Shortage of Attendance (<65%) | AB: Absent in ESE                    ");
        System.out.println("  Formulas: SGPA = Sum(Credits * GradePoints) / Sum(Credits) | Equivalent Percentage = SGPA * 10                       ");
        System.out.println("========================================================================================================================");
    }

    public static void generateAttendanceDefaulterReportUI() throws SQLException {
        System.out.println("\n========================================================================================================================");
        System.out.println("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                     ");
        System.out.println("                                  ATTENDANCE DEFAULTER REPORT                                           ");
        System.out.println("              (Regulation 2024: Attendance < 65% Awarded 'SA' Grade & Barred from ESE)                  ");
        System.out.println("========================================================================================================================");

        List<CourseEvaluationResult> defaulters = academicDAO.getAttendanceDefaulters();
        if (defaulters.isEmpty()) {
            System.out.println("[INFO] No attendance defaulters found! All enrolled students meet eligibility criteria.");
            System.out.println("========================================================================================================================");
            return;
        }

        System.out.printf("%-10s | %-10s | %-32s | %-12s | %-12s | %-10s | %-20s\n",
                "COURSE", "TYPE", "COURSE TITLE", "CLASSES", "ATTENDANCE %", "GRADE", "DEFICIENCY STATUS");
        System.out.println("------------------------------------------------------------------------------------------------------------------------");

        for (CourseEvaluationResult cr : defaulters) {
            String status = cr.getAttendancePercentage() < 65.0 ?
                    "BARRED (< 65%)" : "CONDONATION PENDING";
            System.out.printf("%-10s | %-10s | %-32s | %3d / %-3d   | %6.1f%%      | %-10s | %-20s\n",
                    cr.getCourseCode(),
                    cr.getCourseType(),
                    truncate(cr.getCourseTitle(), 32),
                    cr.getAttendedClasses(),
                    cr.getTotalClasses(),
                    cr.getAttendancePercentage(),
                    cr.getLetterGrade(),
                    status);
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------------");
        System.out.println("Total Flagged Defaulters: " + defaulters.size());
        System.out.println("NOTE: Per KEC R2024, students with grade 'SA' cannot register for ESE and must re-register the course.");
        System.out.println("========================================================================================================================");
    }

    public static void generateClassSummaryUI() throws SQLException {
        System.out.println(">>> 3.3 Class Performance & Result Summary");
        String dept = readString("Enter Department (or 'ALL' for college-wide summary): ").toUpperCase();
        int sem = readInt("Enter Semester (1-8): ", 1, 8);

        ClassPerformanceSummary summary = academicDAO.getClassPerformanceSummary(dept, sem);

        System.out.println("\n=========================================================================================");
        System.out.println("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                      ");
        System.out.println("                              CLASS PERFORMANCE & RESULT SUMMARY                         ");
        System.out.println("                                  REGULATION 2024 (R2024)                                ");
        System.out.println("=========================================================================================");
        System.out.printf("  Department : %-20s  Semester : %-15s\n", summary.getDepartment(), "Semester " + sem);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("  Total Students Evaluated    : %d\n", summary.getTotalStudents());
        System.out.printf("  Students Cleared All Courses: %d (%.2f%% Pass Rate)\n",
                summary.getTotalStudentsAllClear(), summary.getPassPercentage());
        System.out.printf("  Students with Arrears (RA/SA): %d\n", summary.getTotalStudentsWithArrear());
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("  Total Course Enrollments    : %d\n", summary.getTotalEnrollments());
        System.out.printf("  Total Courses Passed        : %d\n", summary.getTotalPassedCourses());
        System.out.printf("  Total Courses Failed        : %d\n", summary.getTotalFailedCourses());
        System.out.printf("  CLASS AVERAGE SGPA          : %.2f / 10.00\n", summary.getClassAverageSgpa());
        System.out.printf("  CLASS AVERAGE PERCENTAGE    : %.2f%%\n", summary.getClassAverageSgpa() * 10.0);
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.println("  Grade Distribution Count:");
        Map<String, Integer> dist = summary.getGradeDistribution();
        System.out.printf("    O  (Outstanding) : %-3d   A+ (Excellent)   : %-3d   A  (Very Good) : %-3d\n",
                dist.getOrDefault("O", 0), dist.getOrDefault("A+", 0), dist.getOrDefault("A", 0));
        System.out.printf("    B+ (Good)        : %-3d   B  (Average)     : %-3d   RA (Re-Appear) : %-3d\n",
                dist.getOrDefault("B+", 0), dist.getOrDefault("B", 0), dist.getOrDefault("RA", 0));
        System.out.printf("    SA (Shortage Att): %-3d   AB (Absent ESE)  : %-3d\n",
                dist.getOrDefault("SA", 0), dist.getOrDefault("AB", 0));
        System.out.println("=========================================================================================");
    }

    // =========================================================================
    // 4. DATABASE & UTILITIES
    // =========================================================================

    private static void handleDatabaseAndUtilities() {
        boolean back = false;
        while (!back) {
            System.out.println("\n-----------------------------------------------------------------------------------------");
            System.out.println("              4. SYSTEM UTILITIES & SAMPLE DATA LOADER                                   ");
            System.out.println("-----------------------------------------------------------------------------------------");
            System.out.println("  [1] Load Realistic KEC R2024 Demonstration Data (Students, Courses, Marks)");
            System.out.println("  [2] View Database Status & Active Connection Info");
            System.out.println("  [3] Reset / Re-initialize Entire Database Schema");
            System.out.println("  [4] Return to Main Menu");
            System.out.println("-----------------------------------------------------------------------------------------");

            int choice = readInt("Select option (1-4): ", 1, 4);
            System.out.println();

            try {
                switch (choice) {
                    case 1:
                        academicDAO.loadSampleDemonstrationData();
                        System.out.println("[SUCCESS] Sample demonstration data loaded successfully!");
                        System.out.println("          Includes students (24ITR001, 24ITR002, 24ITR003, etc.)");
                        System.out.println("          Courses (24ITT31 Theory, 24ITP32 Practical, 24ITE33 Embedded)");
                        System.out.println("          Demonstrating O/A+ grades, Condonation approvals, SA defaulters, and RA fails.");
                        break;
                    case 2:
                        System.out.println("Database Engine : " + DBConnection.getDatabaseType().toUpperCase());
                        System.out.println("Connection URL  : " + DBConnection.getUrl());
                        System.out.println("Total Students  : " + studentDAO.getStudentCount());
                        System.out.println("Total Courses   : " + academicDAO.getAllCourses().size());
                        System.out.println("Total Enrollments: " + academicDAO.getAllEnrollments().size());
                        break;
                    case 3:
                        String confirm = readString("Are you sure you want to reset all database tables? (Y/N): ").toUpperCase();
                        if (confirm.startsWith("Y")) {
                            DBConnection.resetDatabase();
                            System.out.println("[SUCCESS] Database tables reset and re-initialized cleanly.");
                        } else {
                            System.out.println("[INFO] Reset cancelled.");
                        }
                        break;
                    case 4:
                        back = true;
                        break;
                }
            } catch (Exception e) {
                System.err.println("[ERROR] Utility operation failed: " + e.getMessage());
            }
        }
    }

    // =========================================================================
    // HELPER INPUT UTILITIES
    // =========================================================================

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("[INVALID] Please enter an integer between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[INVALID] Input must be a valid integer number.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("[INVALID] Please enter a value between %.2f and %.2f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[INVALID] Input must be a valid decimal number.");
            }
        }
    }

    private static void pressEnterToContinue() {
        System.out.print("\nPress [Enter] to continue...");
        scanner.nextLine();
    }

    private static String truncate(String s, int maxLen) {
        if (s == null) return "";
        if (s.length() <= maxLen) return s;
        return s.substring(0, maxLen - 3) + "...";
    }
}
