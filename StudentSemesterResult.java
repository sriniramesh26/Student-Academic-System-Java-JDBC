import java.util.ArrayList;
import java.util.List;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Aggregated Semester Grade Sheet & Academic Performance Report.
 * Includes SGPA, Equivalent Percentage, Credits, and Course Breakdown.
 */
public class StudentSemesterResult {
    private Student student;
    private int semester;
    private List<CourseEvaluationResult> courseResults = new ArrayList<>();
    private int totalCreditsEnrolled = 0;
    private int totalCreditsEarned = 0;
    private int totalCreditPoints = 0;
    private double sgpa = 0.0;
    private double equivalentPercentage = 0.0;
    private boolean passedSemester = true;
    private int passedCoursesCount = 0;
    private int failedCoursesCount = 0;

    public StudentSemesterResult() {
    }

    public StudentSemesterResult(Student student, int semester) {
        this.student = student;
        this.semester = semester;
    }

    public void addCourseResult(CourseEvaluationResult result) {
        courseResults.add(result);
        totalCreditsEnrolled += result.getCredits();
        totalCreditPoints += result.getCreditPoints();

        if ("PASS".equalsIgnoreCase(result.getResultStatus())) {
            totalCreditsEarned += result.getCredits();
            passedCoursesCount++;
        } else {
            passedSemester = false;
            failedCoursesCount++;
        }

        recalculateSGPA();
    }

    private void recalculateSGPA() {
        if (totalCreditsEnrolled > 0) {
            this.sgpa = (double) totalCreditPoints / totalCreditsEnrolled;
            // SGPA rounded to 2 decimal places
            this.sgpa = Math.round(this.sgpa * 100.0) / 100.0;
            // Equivalent Percentage = SGPA * 10
            this.equivalentPercentage = Math.round(this.sgpa * 10.0 * 100.0) / 100.0;
        } else {
            this.sgpa = 0.0;
            this.equivalentPercentage = 0.0;
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public List<CourseEvaluationResult> getCourseResults() {
        return courseResults;
    }

    public void setCourseResults(List<CourseEvaluationResult> courseResults) {
        this.courseResults = courseResults;
    }

    public int getTotalCreditsEnrolled() {
        return totalCreditsEnrolled;
    }

    public int getTotalCreditsEarned() {
        return totalCreditsEarned;
    }

    public int getTotalCreditPoints() {
        return totalCreditPoints;
    }

    public double getSgpa() {
        return sgpa;
    }

    public double getEquivalentPercentage() {
        return equivalentPercentage;
    }

    public boolean isPassedSemester() {
        return passedSemester;
    }

    public int getPassedCoursesCount() {
        return passedCoursesCount;
    }

    public int getFailedCoursesCount() {
        return failedCoursesCount;
    }
}
