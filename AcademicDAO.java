import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Academic Data Access Object (AcademicDAO)
 * Implements course management, enrollment & attendance tracking,
 * phased incremental marks entry ("Then and Now"), and KEC R2024
 * Evaluation & Weightage Engine.
 */
public class AcademicDAO {

    // =========================================================================
    // 1. COURSE CATALOG MANAGEMENT
    // =========================================================================

    public boolean addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses (course_code, title, credits, course_type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, course.getCourseCode().trim().toUpperCase());
            pstmt.setString(2, course.getTitle().trim());
            pstmt.setInt(3, course.getCredits());
            pstmt.setString(4, course.getCourseType().trim().toUpperCase());
            return pstmt.executeUpdate() > 0;
        }
    }

    public Course getCourseByCode(String courseCode) throws SQLException {
        String sql = "SELECT course_code, title, credits, course_type FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                            rs.getString("course_code"),
                            rs.getString("title"),
                            rs.getInt("credits"),
                            rs.getString("course_type")
                    );
                }
            }
        }
        return null;
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_code, title, credits, course_type FROM courses ORDER BY course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getString("course_type")
                ));
            }
        }
        return list;
    }

    public boolean deleteCourse(String courseCode) throws SQLException {
        String sql = "DELETE FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, courseCode.trim().toUpperCase());
            return pstmt.executeUpdate() > 0;
        }
    }

    // =========================================================================
    // 2. ENROLLMENT & ATTENDANCE TRACKING
    // =========================================================================

    /**
     * Enrolls a student into a course for a specific semester and initializes
     * the assessment_marks row for incremental entry.
     */
    public int enrollStudent(String rollNo, String courseCode, int semester,
                             int totalClasses, int attendedClasses, boolean medicalCondonation) throws SQLException {
        String insertEnrollmentSql = "INSERT INTO enrollments (roll_no, course_code, semester, total_classes, attended_classes, medical_condonation) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        int enrollmentId = -1;

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement pstmt = conn.prepareStatement(insertEnrollmentSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, rollNo.trim().toUpperCase());
                pstmt.setString(2, courseCode.trim().toUpperCase());
                pstmt.setInt(3, semester);
                pstmt.setInt(4, totalClasses);
                pstmt.setInt(5, attendedClasses);
                pstmt.setInt(6, medicalCondonation ? 1 : 0);
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        enrollmentId = rs.getInt(1);
                    }
                }
            }

            // In SQLite, if RETURN_GENERATED_KEYS didn't yield, query last_insert_rowid()
            if (enrollmentId <= 0) {
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()")) {
                    if (rs.next()) {
                        enrollmentId = rs.getInt(1);
                    }
                }
            }

            // Create initial assessment_marks placeholder row
            if (enrollmentId > 0) {
                String insertMarksSql = "INSERT INTO assessment_marks (enrollment_id, is_absent_ese) VALUES (?, 0)";
                try (PreparedStatement pstmtMarks = conn.prepareStatement(insertMarksSql)) {
                    pstmtMarks.setInt(1, enrollmentId);
                    pstmtMarks.executeUpdate();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return enrollmentId;
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Continuously records total classes held vs. classes attended.
     */
    public boolean updateAttendance(int enrollmentId, int totalClasses, int attendedClasses) throws SQLException {
        String sql = "UPDATE enrollments SET total_classes = ?, attended_classes = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, totalClasses);
            pstmt.setInt(2, attendedClasses);
            pstmt.setInt(3, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates medical condonation approval for attendance 65% - 74.9%.
     */
    public boolean setMedicalCondonation(int enrollmentId, boolean approved) throws SQLException {
        String sql = "UPDATE enrollments SET medical_condonation = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, approved ? 1 : 0);
            pstmt.setInt(2, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Enrollment getEnrollmentById(int enrollmentId) throws SQLException {
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, " +
                "e.attended_classes, e.medical_condonation, s.name AS student_name, " +
                "c.title AS course_title, c.course_type, c.credits " +
                "FROM enrollments e " +
                "JOIN students s ON e.roll_no = s.roll_no " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "WHERE e.enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapEnrollment(rs);
                }
            }
        }
        return null;
    }

    public Enrollment getEnrollment(String rollNo, String courseCode, int semester) throws SQLException {
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, " +
                "e.attended_classes, e.medical_condonation, s.name AS student_name, " +
                "c.title AS course_title, c.course_type, c.credits " +
                "FROM enrollments e " +
                "JOIN students s ON e.roll_no = s.roll_no " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "WHERE e.roll_no = ? AND e.course_code = ? AND e.semester = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo.trim().toUpperCase());
            pstmt.setString(2, courseCode.trim().toUpperCase());
            pstmt.setInt(3, semester);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapEnrollment(rs);
                }
            }
        }
        return null;
    }

    public List<Enrollment> getEnrollmentsByStudent(String rollNo, int semester) throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, " +
                "e.attended_classes, e.medical_condonation, s.name AS student_name, " +
                "c.title AS course_title, c.course_type, c.credits " +
                "FROM enrollments e " +
                "JOIN students s ON e.roll_no = s.roll_no " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "WHERE e.roll_no = ? AND e.semester = ? " +
                "ORDER BY c.course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo.trim().toUpperCase());
            pstmt.setInt(2, semester);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapEnrollment(rs));
                }
            }
        }
        return list;
    }

    public List<Enrollment> getAllEnrollments() throws SQLException {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT e.enrollment_id, e.roll_no, e.course_code, e.semester, e.total_classes, " +
                "e.attended_classes, e.medical_condonation, s.name AS student_name, " +
                "c.title AS course_title, c.course_type, c.credits " +
                "FROM enrollments e " +
                "JOIN students s ON e.roll_no = s.roll_no " +
                "JOIN courses c ON e.course_code = c.course_code " +
                "ORDER BY e.roll_no ASC, e.course_code ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapEnrollment(rs));
            }
        }
        return list;
    }

    private Enrollment mapEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getString("roll_no"),
                rs.getString("course_code"),
                rs.getInt("semester"),
                rs.getInt("total_classes"),
                rs.getInt("attended_classes"),
                rs.getInt("medical_condonation") == 1
        );
        e.setStudentName(rs.getString("student_name"));
        e.setCourseTitle(rs.getString("course_title"));
        e.setCourseType(rs.getString("course_type"));
        e.setCredits(rs.getInt("credits"));
        return e;
    }

    // =========================================================================
    // 3. INCREMENTAL MARKS ENTRY ("THEN AND NOW")
    // =========================================================================

    /**
     * Phase 1 (Mid-Semester): Record CIA Test 1 (0–100) and CIA Test 2 (0–100).
     */
    public boolean recordPhase1Marks(int enrollmentId, Double cia1, Double cia2) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET cia_1 = ?, cia_2 = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (cia1 != null) pstmt.setDouble(1, cia1); else pstmt.setNull(1, java.sql.Types.DECIMAL);
            if (cia2 != null) pstmt.setDouble(2, cia2); else pstmt.setNull(2, java.sql.Types.DECIMAL);
            pstmt.setInt(3, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Phase 2 (End of Semester - CIA): Record Assignments / Quizzes / Lab Evaluations (0–100).
     */
    public boolean recordPhase2Marks(int enrollmentId, Double assignmentMarks) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET assignment_marks = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (assignmentMarks != null) pstmt.setDouble(1, assignmentMarks); else pstmt.setNull(1, java.sql.Types.DECIMAL);
            pstmt.setInt(2, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Phase 3 (Post Semester): Record End Semester Examination (ESE) Marks (0–100) and Mark Absence (AB).
     */
    public boolean recordPhase3Marks(int enrollmentId, Double eseMarks, boolean isAbsentEse) throws SQLException {
        ensureMarksRecordExists(enrollmentId);
        String sql = "UPDATE assessment_marks SET ese_marks = ?, is_absent_ese = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (isAbsentEse || eseMarks == null) {
                pstmt.setNull(1, java.sql.Types.DECIMAL);
                pstmt.setInt(2, 1);
            } else {
                pstmt.setDouble(1, eseMarks);
                pstmt.setInt(2, 0);
            }
            pstmt.setInt(3, enrollmentId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public AssessmentMarks getMarksByEnrollmentId(int enrollmentId) throws SQLException {
        String sql = "SELECT mark_id, enrollment_id, cia_1, cia_2, assignment_marks, ese_marks, is_absent_ese " +
                "FROM assessment_marks WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Double cia1 = rs.getObject("cia_1") != null ? rs.getDouble("cia_1") : null;
                    Double cia2 = rs.getObject("cia_2") != null ? rs.getDouble("cia_2") : null;
                    Double assign = rs.getObject("assignment_marks") != null ? rs.getDouble("assignment_marks") : null;
                    Double ese = rs.getObject("ese_marks") != null ? rs.getDouble("ese_marks") : null;
                    boolean absent = rs.getInt("is_absent_ese") == 1;
                    return new AssessmentMarks(rs.getInt("mark_id"), enrollmentId, cia1, cia2, assign, ese, absent);
                }
            }
        }
        return null;
    }

    private void ensureMarksRecordExists(int enrollmentId) throws SQLException {
        String checkSql = "SELECT mark_id FROM assessment_marks WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkSql)) {
            pstmt.setInt(1, enrollmentId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    String insertSql = "INSERT INTO assessment_marks (enrollment_id, is_absent_ese) VALUES (?, 0)";
                    try (PreparedStatement insertPstmt = conn.prepareStatement(insertSql)) {
                        insertPstmt.setInt(1, enrollmentId);
                        insertPstmt.executeUpdate();
                    }
                }
            }
        }
    }

    // =========================================================================
    // 4. KEC R2024 EVALUATION & WEIGHTAGE ENGINE
    // =========================================================================

    /**
     * Evaluates a single course enrollment according to Kongu Engineering College
     * Regulation 2024 specifications:
     * - Attendance filtering (<65% SA barred, 65-74.9% conditional condonation, >=75% eligible)
     * - Course weightage engine:
     *     THEORY: CIA 40% | ESE 60% | ESE Min = 45% (27/60) | Overall Min = 50
     *     PRACTICAL: CIA 60% | ESE 40% | ESE Min = 50% (20/40) | Overall Min = 50
     *     EMBEDDED: CIA 50% | ESE 50% | ESE Min = 45% (22.5/50) | Overall Min = 50
     * - 10-Point Grading System (O, A+, A, B+, B, RA, SA, AB)
     */
    public CourseEvaluationResult evaluateCourse(Enrollment enrollment, AssessmentMarks marks, Course course) {
        CourseEvaluationResult result = new CourseEvaluationResult();
        result.setCourseCode(course.getCourseCode());
        result.setCourseTitle(course.getTitle());
        result.setCourseType(course.getCourseType());
        result.setCredits(course.getCredits());

        result.setTotalClasses(enrollment.getTotalClasses());
        result.setAttendedClasses(enrollment.getAttendedClasses());
        double attPct = enrollment.getAttendancePercentage();
        result.setAttendancePercentage(Math.round(attPct * 100.0) / 100.0);
        result.setMedicalCondonation(enrollment.isMedicalCondonation());

        // Attendance Filter Rules
        if (attPct >= 75.0) {
            result.setAttendanceStatus("ELIGIBLE");
            result.setBarred(false);
        } else if (attPct >= 65.0 && attPct < 75.0) {
            if (enrollment.isMedicalCondonation()) {
                result.setAttendanceStatus("CONDONED");
                result.setBarred(false);
            } else {
                result.setAttendanceStatus("SHORTAGE (SA)");
                result.setBarred(true);
            }
        } else {
            result.setAttendanceStatus("SHORTAGE (SA)");
            result.setBarred(true);
        }

        // Configure Weightages & Thresholds by Course Type
        double ciaWeight = 40.0;
        double eseWeight = 60.0;
        double eseMin = 27.0; // 45% of 60

        String cType = course.getCourseType().toUpperCase();
        if ("PRACTICAL".equals(cType)) {
            ciaWeight = 60.0;
            eseWeight = 40.0;
            eseMin = 20.0; // 50% of 40
        } else if ("EMBEDDED".equals(cType)) {
            ciaWeight = 50.0;
            eseWeight = 50.0;
            eseMin = 22.5; // 45% of 50
        }

        result.setCiaWeightage(ciaWeight);
        result.setEseWeightage(eseWeight);
        result.setEseMinThreshold(eseMin);

        // Process Marks if available
        Double cia1 = (marks != null) ? marks.getCia1() : null;
        Double cia2 = (marks != null) ? marks.getCia2() : null;
        Double assign = (marks != null) ? marks.getAssignmentMarks() : null;
        Double ese = (marks != null) ? marks.getEseMarks() : null;
        boolean isAbsent = (marks != null) && marks.isAbsentEse();

        result.setCia1(cia1);
        result.setCia2(cia2);
        result.setAssignmentMarks(assign);
        result.setEseMarks(ese);
        result.setAbsent(isAbsent);

        // Compute Raw CIA out of 100 based on internal component weights
        double rawCia100 = 0.0;
        double c1 = (cia1 != null) ? cia1 : 0.0;
        double c2 = (cia2 != null) ? cia2 : 0.0;
        double asgn = (assign != null) ? assign : 0.0;
        double testAvg = (c1 + c2) / 2.0;

        if ("THEORY".equals(cType)) {
            // Theory: Tests 60% of CIA, Assignments/Quizzes 40% of CIA
            rawCia100 = (testAvg * 0.60) + (asgn * 0.40);
        } else if ("PRACTICAL".equals(cType)) {
            // Practical: Model tests 40% of CIA, Lab continuous evaluation 60% of CIA
            rawCia100 = (testAvg * 0.40) + (asgn * 0.60);
        } else {
            // Embedded: Theory tests 50% of CIA, Practical/Project 50% of CIA
            rawCia100 = (testAvg * 0.50) + (asgn * 0.50);
        }
        result.setRawCia100(Math.round(rawCia100 * 100.0) / 100.0);

        // Scale CIA to Course Type Weightage
        double ciaScaled = rawCia100 * (ciaWeight / 100.0);
        result.setCiaScaled(Math.round(ciaScaled * 100.0) / 100.0);

        // CASE 1: Barred due to Shortage of Attendance (SA)
        if (result.isBarred()) {
            result.setEseScaled(0.0);
            result.setTotalMarks(0.0);
            result.setLetterGrade("SA");
            result.setGradePoints(0);
            result.setCreditPoints(0);
            result.setResultStatus("FAIL (SA)");
            result.setRemark("Barred from ESE (Attendance < 65% or Condonation Pending)");
            result.setEseMinMet(false);
            return result;
        }

        // CASE 2: Marked Absent in ESE (AB)
        if (isAbsent || ese == null) {
            result.setEseScaled(0.0);
            result.setTotalMarks(Math.round(ciaScaled));
            result.setLetterGrade("AB");
            result.setGradePoints(0);
            result.setCreditPoints(0);
            result.setResultStatus("ABSENT (AB)");
            result.setRemark("Absent in End Semester Examination");
            result.setEseMinMet(false);
            return result;
        }

        // CASE 3: Standard Evaluation
        double eseScaled = ese * (eseWeight / 100.0);
        result.setEseScaled(Math.round(eseScaled * 100.0) / 100.0);

        boolean eseMinMet = eseScaled >= (eseMin - 0.001); // threshold check
        result.setEseMinMet(eseMinMet);

        double totalMarks = ciaScaled + eseScaled;
        // Total rounded to nearest integer for standard grade bracket matching
        double roundedTotal = Math.round(totalMarks);
        result.setTotalMarks(roundedTotal);

        if (!eseMinMet) {
            result.setLetterGrade("RA");
            result.setGradePoints(0);
            result.setCreditPoints(0);
            result.setResultStatus("FAIL (RA)");
            result.setRemark("Failed ESE Minimum Threshold (" + String.format("%.1f/%.0f", eseScaled, eseWeight) + " < " + eseMin + ")");
        } else if (roundedTotal < 50.0) {
            result.setLetterGrade("RA");
            result.setGradePoints(0);
            result.setCreditPoints(0);
            result.setResultStatus("FAIL (RA)");
            result.setRemark("Aggregate marks < 50 minimum pass mark");
        } else {
            // 10-Point Grading System
            if (roundedTotal >= 91.0) {
                result.setLetterGrade("O");
                result.setGradePoints(10);
            } else if (roundedTotal >= 81.0) {
                result.setLetterGrade("A+");
                result.setGradePoints(9);
            } else if (roundedTotal >= 71.0) {
                result.setLetterGrade("A");
                result.setGradePoints(8);
            } else if (roundedTotal >= 61.0) {
                result.setLetterGrade("B+");
                result.setGradePoints(7);
            } else { // 50 to 60
                result.setLetterGrade("B");
                result.setGradePoints(6);
            }
            result.setCreditPoints(result.getCredits() * result.getGradePoints());
            result.setResultStatus("PASS");
            result.setRemark("Qualified");
        }

        return result;
    }

    /**
     * Generates a complete Student Semester Grade Sheet with SGPA and percentage.
     */
    public StudentSemesterResult generateStudentSemesterReport(String rollNo, int semester) throws SQLException {
        StudentDAO sdao = new StudentDAO();
        Student student = sdao.getStudentByRollNo(rollNo);
        if (student == null) {
            return null;
        }

        StudentSemesterResult report = new StudentSemesterResult(student, semester);
        List<Enrollment> enrollments = getEnrollmentsByStudent(rollNo, semester);

        for (Enrollment enroll : enrollments) {
            Course course = getCourseByCode(enroll.getCourseCode());
            AssessmentMarks marks = getMarksByEnrollmentId(enroll.getEnrollmentId());
            CourseEvaluationResult evalResult = evaluateCourse(enroll, marks, course);
            report.addCourseResult(evalResult);
        }

        return report;
    }

    /**
     * Generates Attendance Defaulter Report:
     * Lists all students with attendance < 65% (flagged with grade SA)
     * and students with 65-74.9% without medical condonation approval.
     */
    public List<CourseEvaluationResult> getAttendanceDefaulters() throws SQLException {
        List<CourseEvaluationResult> defaulters = new ArrayList<>();
        List<Enrollment> all = getAllEnrollments();

        for (Enrollment enroll : all) {
            Course course = getCourseByCode(enroll.getCourseCode());
            AssessmentMarks marks = getMarksByEnrollmentId(enroll.getEnrollmentId());
            CourseEvaluationResult eval = evaluateCourse(enroll, marks, course);

            // Flagged if barred due to attendance shortage
            if (eval.isBarred() || "SA".equals(eval.getLetterGrade()) || eval.getAttendancePercentage() < 65.0) {
                defaulters.add(eval);
            }
        }
        return defaulters;
    }

    /**
     * Generates Class Performance & Result Summary:
     * Department-wise & semester-wise pass/fail count and class average SGPA.
     */
    public ClassPerformanceSummary getClassPerformanceSummary(String department, int semester) throws SQLException {
        StudentDAO sdao = new StudentDAO();
        List<Student> students;
        if (department != null && !department.trim().isEmpty() && !"ALL".equalsIgnoreCase(department)) {
            students = sdao.getStudentsByDepartment(department);
        } else {
            students = sdao.getAllStudents();
        }

        ClassPerformanceSummary summary = new ClassPerformanceSummary(department, semester);
        double totalSgpaSum = 0.0;
        int evaluatedStudentsCount = 0;

        for (Student st : students) {
            StudentSemesterResult semReport = generateStudentSemesterReport(st.getRollNo(), semester);
            if (semReport != null && !semReport.getCourseResults().isEmpty()) {
                summary.setTotalStudents(summary.getTotalStudents() + 1);
                evaluatedStudentsCount++;
                totalSgpaSum += semReport.getSgpa();

                if (semReport.isPassedSemester()) {
                    summary.setTotalStudentsAllClear(summary.getTotalStudentsAllClear() + 1);
                } else {
                    summary.setTotalStudentsWithArrear(summary.getTotalStudentsWithArrear() + 1);
                }

                for (CourseEvaluationResult cr : semReport.getCourseResults()) {
                    summary.setTotalEnrollments(summary.getTotalEnrollments() + 1);
                    if ("PASS".equalsIgnoreCase(cr.getResultStatus())) {
                        summary.setTotalPassedCourses(summary.getTotalPassedCourses() + 1);
                    } else {
                        summary.setTotalFailedCourses(summary.getTotalFailedCourses() + 1);
                    }
                    summary.addGrade(cr.getLetterGrade());
                }
            }
        }

        if (evaluatedStudentsCount > 0) {
            double avgSgpa = totalSgpaSum / evaluatedStudentsCount;
            summary.setClassAverageSgpa(Math.round(avgSgpa * 100.0) / 100.0);
            double passPct = (summary.getTotalStudentsAllClear() * 100.0) / evaluatedStudentsCount;
            summary.setPassPercentage(Math.round(passPct * 100.0) / 100.0);
        }

        return summary;
    }

    // =========================================================================
    // 5. SAMPLE DEMONSTRATION DATA LOADER
    // =========================================================================

    /**
     * Populates comprehensive sample demonstration data representing
     * all facets of KEC R2024 (Theory, Practical, Embedded, High scorers,
     * Condonation approvals, Defaulters with SA grade, and Absent ESE).
     */
    public void loadSampleDemonstrationData() throws SQLException {
        StudentDAO sdao = new StudentDAO();

        // Safely remove previous demo records if present to allow idempotent reloads
        String[] demoRolls = {"24ITR001", "24ITR002", "24ITR003", "24CSR015", "24ECR042"};
        for (String r : demoRolls) {
            sdao.deleteStudent(r);
        }
        String[] demoCourses = {"24ITT31", "24ITP32", "24ITE33", "24CST31"};
        for (String c : demoCourses) {
            deleteCourse(c);
        }

        // 1. Add Sample Students
        sdao.addStudent(new Student("24ITR001", "Aravind Kumar S", "IT", 3));
        sdao.addStudent(new Student("24ITR002", "Bhavani Devi M", "IT", 3));
        sdao.addStudent(new Student("24ITR003", "Chandru P", "IT", 3));
        sdao.addStudent(new Student("24CSR015", "Deepak Raja V", "CSE", 3));
        sdao.addStudent(new Student("24ECR042", "Elango K", "ECE", 3));

        // 2. Add Sample Courses
        addCourse(new Course("24ITT31", "Database Management Systems", 3, "THEORY"));
        addCourse(new Course("24ITP32", "Data Structures & Algorithms Laboratory", 2, "PRACTICAL"));
        addCourse(new Course("24ITE33", "Object-Oriented Programming with Java", 4, "EMBEDDED"));
        addCourse(new Course("24CST31", "Computer Organization & Architecture", 3, "THEORY"));

        // 3. Enroll Students and Enter Marks
        // Student 1: 24ITR001 (Outstanding performer)
        int e1 = enrollStudent("24ITR001", "24ITT31", 3, 50, 47, false);
        recordPhase1Marks(e1, 95.0, 96.0);
        recordPhase2Marks(e1, 92.0);
        recordPhase3Marks(e1, 94.0, false);

        int e2 = enrollStudent("24ITR001", "24ITP32", 3, 30, 29, false);
        recordPhase1Marks(e2, 98.0, 95.0);
        recordPhase2Marks(e2, 96.0);
        recordPhase3Marks(e2, 92.0, false);

        int e3 = enrollStudent("24ITR001", "24ITE33", 3, 60, 56, false);
        recordPhase1Marks(e3, 90.0, 92.0);
        recordPhase2Marks(e3, 94.0);
        recordPhase3Marks(e3, 90.0, false);

        // Student 2: 24ITR002 (Condonation Case: 65% - 74.9% Approved)
        int e4 = enrollStudent("24ITR002", "24ITT31", 3, 50, 36, true); // 72% + Condoned
        recordPhase1Marks(e4, 76.0, 78.0);
        recordPhase2Marks(e4, 80.0);
        recordPhase3Marks(e4, 74.0, false);

        int e5 = enrollStudent("24ITR002", "24ITP32", 3, 30, 27, false); // 90%
        recordPhase1Marks(e5, 82.0, 80.0);
        recordPhase2Marks(e5, 85.0);
        recordPhase3Marks(e5, 78.0, false);

        int e6 = enrollStudent("24ITR002", "24ITE33", 3, 60, 44, true); // 73.3% + Condoned
        recordPhase1Marks(e6, 68.0, 70.0);
        recordPhase2Marks(e6, 75.0);
        recordPhase3Marks(e6, 66.0, false);

        // Student 3: 24ITR003 (Defaulter <65% -> SA Grade, and Failed Threshold)
        int e7 = enrollStudent("24ITR003", "24ITT31", 3, 50, 26, false); // 52% -> SA Grade!
        recordPhase1Marks(e7, 45.0, 50.0);
        recordPhase2Marks(e7, 55.0);
        recordPhase3Marks(e7, null, true); // Barred

        int e8 = enrollStudent("24ITR003", "24ITP32", 3, 30, 25, false); // 83.3%
        recordPhase1Marks(e8, 70.0, 72.0);
        recordPhase2Marks(e8, 75.0);
        recordPhase3Marks(e8, 35.0, false); // ESE 35% -> 14/40 < 20 Min Threshold -> RA!

        int e9 = enrollStudent("24ITR003", "24ITE33", 3, 60, 32, false); // 53.3% -> SA Grade!
        recordPhase1Marks(e9, 40.0, 45.0);
        recordPhase2Marks(e9, 50.0);
        recordPhase3Marks(e9, null, true); // Barred
    }
}
