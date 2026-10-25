/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Model class representing a Student's Enrollment in a Course and Attendance status.
 */
public class Enrollment {
    private int enrollmentId;
    private String rollNo;
    private String courseCode;
    private int semester;
    private int totalClasses;
    private int attendedClasses;
    private boolean medicalCondonation;

    // Joined fields for display convenience
    private String studentName;
    private String courseTitle;
    private String courseType;
    private int credits;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, String rollNo, String courseCode, int semester,
                      int totalClasses, int attendedClasses, boolean medicalCondonation) {
        this.enrollmentId = enrollmentId;
        this.rollNo = rollNo;
        this.courseCode = courseCode;
        this.semester = semester;
        this.totalClasses = totalClasses;
        this.attendedClasses = attendedClasses;
        this.medicalCondonation = medicalCondonation;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public int getAttendedClasses() {
        return attendedClasses;
    }

    public void setAttendedClasses(int attendedClasses) {
        this.attendedClasses = attendedClasses;
    }

    public boolean isMedicalCondonation() {
        return medicalCondonation;
    }

    public void setMedicalCondonation(boolean medicalCondonation) {
        this.medicalCondonation = medicalCondonation;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public double getAttendancePercentage() {
        if (totalClasses <= 0) return 100.0;
        return (attendedClasses * 100.0) / totalClasses;
    }
}
