/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Evaluated result for a single course enrollment according to R2024 weightage and grading rules.
 */
public class CourseEvaluationResult {
    private String courseCode;
    private String courseTitle;
    private String courseType; // THEORY, PRACTICAL, EMBEDDED
    private int credits;

    // Attendance
    private int totalClasses;
    private int attendedClasses;
    private double attendancePercentage;
    private boolean medicalCondonation;
    private String attendanceStatus; // ELIGIBLE, CONDONED, SHORTAGE (SA)
    private boolean isBarred;

    // Marks
    private Double cia1;
    private Double cia2;
    private Double assignmentMarks;
    private double rawCia100;
    private double ciaScaled;
    private double ciaWeightage;

    private Double eseMarks;
    private boolean isAbsent;
    private double eseScaled;
    private double eseWeightage;
    private double eseMinThreshold;
    private boolean eseMinMet;

    private double totalMarks;
    private String letterGrade;   // O, A+, A, B+, B, RA, SA, AB
    private int gradePoints;      // 10, 9, 8, 7, 6, 0
    private int creditPoints;     // credits * gradePoints
    private String resultStatus;  // PASS, FAIL (RA), FAIL (SA), ABSENT (AB)
    private String remark;

    public CourseEvaluationResult() {
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }

    public boolean isMedicalCondonation() {
        return medicalCondonation;
    }

    public void setMedicalCondonation(boolean medicalCondonation) {
        this.medicalCondonation = medicalCondonation;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public boolean isBarred() {
        return isBarred;
    }

    public void setBarred(boolean barred) {
        isBarred = barred;
    }

    public Double getCia1() {
        return cia1;
    }

    public void setCia1(Double cia1) {
        this.cia1 = cia1;
    }

    public Double getCia2() {
        return cia2;
    }

    public void setCia2(Double cia2) {
        this.cia2 = cia2;
    }

    public Double getAssignmentMarks() {
        return assignmentMarks;
    }

    public void setAssignmentMarks(Double assignmentMarks) {
        this.assignmentMarks = assignmentMarks;
    }

    public double getRawCia100() {
        return rawCia100;
    }

    public void setRawCia100(double rawCia100) {
        this.rawCia100 = rawCia100;
    }

    public double getCiaScaled() {
        return ciaScaled;
    }

    public void setCiaScaled(double ciaScaled) {
        this.ciaScaled = ciaScaled;
    }

    public double getCiaWeightage() {
        return ciaWeightage;
    }

    public void setCiaWeightage(double ciaWeightage) {
        this.ciaWeightage = ciaWeightage;
    }

    public Double getEseMarks() {
        return eseMarks;
    }

    public void setEseMarks(Double eseMarks) {
        this.eseMarks = eseMarks;
    }

    public boolean isAbsent() {
        return isAbsent;
    }

    public void setAbsent(boolean absent) {
        isAbsent = absent;
    }

    public double getEseScaled() {
        return eseScaled;
    }

    public void setEseScaled(double eseScaled) {
        this.eseScaled = eseScaled;
    }

    public double getEseWeightage() {
        return eseWeightage;
    }

    public void setEseWeightage(double eseWeightage) {
        this.eseWeightage = eseWeightage;
    }

    public double getEseMinThreshold() {
        return eseMinThreshold;
    }

    public void setEseMinThreshold(double eseMinThreshold) {
        this.eseMinThreshold = eseMinThreshold;
    }

    public boolean isEseMinMet() {
        return eseMinMet;
    }

    public void setEseMinMet(boolean eseMinMet) {
        this.eseMinMet = eseMinMet;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public int getGradePoints() {
        return gradePoints;
    }

    public void setGradePoints(int gradePoints) {
        this.gradePoints = gradePoints;
    }

    public int getCreditPoints() {
        return creditPoints;
    }

    public void setCreditPoints(int creditPoints) {
        this.creditPoints = creditPoints;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
