import java.util.HashMap;
import java.util.Map;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Model class for Class Performance & Result Summary report.
 */
public class ClassPerformanceSummary {
    private String department;
    private int semester;
    private int totalStudents = 0;
    private int totalEnrollments = 0;
    private int totalPassedCourses = 0;
    private int totalFailedCourses = 0;
    private int totalStudentsAllClear = 0;
    private int totalStudentsWithArrear = 0;
    private double classAverageSgpa = 0.0;
    private double passPercentage = 0.0;
    private Map<String, Integer> gradeDistribution = new HashMap<>();

    public ClassPerformanceSummary() {
        gradeDistribution.put("O", 0);
        gradeDistribution.put("A+", 0);
        gradeDistribution.put("A", 0);
        gradeDistribution.put("B+", 0);
        gradeDistribution.put("B", 0);
        gradeDistribution.put("RA", 0);
        gradeDistribution.put("SA", 0);
        gradeDistribution.put("AB", 0);
    }

    public ClassPerformanceSummary(String department, int semester) {
        this();
        this.department = department;
        this.semester = semester;
    }

    public void addGrade(String grade) {
        if (grade == null) return;
        grade = grade.trim().toUpperCase();
        gradeDistribution.put(grade, gradeDistribution.getOrDefault(grade, 0) + 1);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(int totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }

    public int getTotalPassedCourses() {
        return totalPassedCourses;
    }

    public void setTotalPassedCourses(int totalPassedCourses) {
        this.totalPassedCourses = totalPassedCourses;
    }

    public int getTotalFailedCourses() {
        return totalFailedCourses;
    }

    public void setTotalFailedCourses(int totalFailedCourses) {
        this.totalFailedCourses = totalFailedCourses;
    }

    public int getTotalStudentsAllClear() {
        return totalStudentsAllClear;
    }

    public void setTotalStudentsAllClear(int totalStudentsAllClear) {
        this.totalStudentsAllClear = totalStudentsAllClear;
    }

    public int getTotalStudentsWithArrear() {
        return totalStudentsWithArrear;
    }

    public void setTotalStudentsWithArrear(int totalStudentsWithArrear) {
        this.totalStudentsWithArrear = totalStudentsWithArrear;
    }

    public double getClassAverageSgpa() {
        return classAverageSgpa;
    }

    public void setClassAverageSgpa(double classAverageSgpa) {
        this.classAverageSgpa = classAverageSgpa;
    }

    public double getPassPercentage() {
        return passPercentage;
    }

    public void setPassPercentage(double passPercentage) {
        this.passPercentage = passPercentage;
    }

    public Map<String, Integer> getGradeDistribution() {
        return gradeDistribution;
    }
}
