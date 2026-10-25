/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Model class representing Incremental Assessment Marks.
 * Phase 1: CIA 1, CIA 2
 * Phase 2: Assignments / Quizzes / Lab Evaluations
 * Phase 3: ESE Marks & Absence Indicator
 */
public class AssessmentMarks {
    private int markId;
    private int enrollmentId;
    private Double cia1;            // 0-100 or null if not entered
    private Double cia2;            // 0-100 or null if not entered
    private Double assignmentMarks; // 0-100 or null if not entered
    private Double eseMarks;        // 0-100 or null if not entered
    private boolean isAbsentEse;    // true if absent in ESE

    public AssessmentMarks() {
    }

    public AssessmentMarks(int markId, int enrollmentId, Double cia1, Double cia2,
                           Double assignmentMarks, Double eseMarks, boolean isAbsentEse) {
        this.markId = markId;
        this.enrollmentId = enrollmentId;
        this.cia1 = cia1;
        this.cia2 = cia2;
        this.assignmentMarks = assignmentMarks;
        this.eseMarks = eseMarks;
        this.isAbsentEse = isAbsentEse;
    }

    public int getMarkId() {
        return markId;
    }

    public void setMarkId(int markId) {
        this.markId = markId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
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

    public Double getEseMarks() {
        return eseMarks;
    }

    public void setEseMarks(Double eseMarks) {
        this.eseMarks = eseMarks;
    }

    public boolean isAbsentEse() {
        return isAbsentEse;
    }

    public void setAbsentEse(boolean absentEse) {
        isAbsentEse = absentEse;
    }
}
