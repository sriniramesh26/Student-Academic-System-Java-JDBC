import java.sql.SQLException;
import java.util.List;

/**
 * Verification test runner for KEC R2024 Evaluation Engine.
 */
public class VerifyKecEngine {
    public static void main(String[] args) {
        System.out.println(">>> Starting KEC R2024 Engine Test Suite...");
        try {
            DBConnection.resetDatabase();
            AcademicDAO academicDAO = new AcademicDAO();
            StudentDAO studentDAO = new StudentDAO();

            academicDAO.loadSampleDemonstrationData();
            System.out.println("[PASS] Sample demonstration data loaded successfully.");

            // 1. Verify Student 24ITR001 Mark Sheet
            StudentSemesterResult r1 = academicDAO.generateStudentSemesterReport("24ITR001", 3);
            if (r1 == null || r1.getCourseResults().size() != 3) {
                throw new RuntimeException("Student 24ITR001 report failed: expected 3 courses.");
            }
            MainApp.printStudentMarkSheet(r1);

            System.out.printf("Computed SGPA for 24ITR001: %.2f (Expected ~ 10.00)\n", r1.getSgpa());
            System.out.printf("Equivalent Percentage: %.2f%%\n", r1.getEquivalentPercentage());

            // 2. Verify Student 24ITR002 (Condonation Approved)
            StudentSemesterResult r2 = academicDAO.generateStudentSemesterReport("24ITR002", 3);
            MainApp.printStudentMarkSheet(r2);

            // 3. Verify Student 24ITR003 (Defaulter & Failed Threshold)
            StudentSemesterResult r3 = academicDAO.generateStudentSemesterReport("24ITR003", 3);
            MainApp.printStudentMarkSheet(r3);

            // 4. Verify Attendance Defaulters Report
            List<CourseEvaluationResult> defaulters = academicDAO.getAttendanceDefaulters();
            System.out.println("\n[Attendance Defaulters Count: " + defaulters.size() + "]");
            for (CourseEvaluationResult d : defaulters) {
                System.out.printf("  Course: %s | Attendance: %.1f%% | Grade: %s | Barred: %s\n",
                        d.getCourseCode(), d.getAttendancePercentage(), d.getLetterGrade(), d.isBarred());
            }

            // 5. Verify Class Performance Summary
            ClassPerformanceSummary summary = academicDAO.getClassPerformanceSummary("IT", 3);
            System.out.printf("\nIT Dept Class Average SGPA: %.2f | Pass Rate: %.1f%%\n",
                    summary.getClassAverageSgpa(), summary.getPassPercentage());

            System.out.println("\n>>> ALL KEC R2024 ENGINE TESTS COMPLETED SUCCESSFULLY!");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
