/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Model class representing an Academic Course.
 * Course Types: THEORY, PRACTICAL, EMBEDDED
 */
public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String courseType; // THEORY, PRACTICAL, EMBEDDED

    public Course() {
    }

    public Course(String courseCode, String title, int credits, String courseType) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
        this.title = title != null ? title.trim() : "";
        this.credits = credits;
        this.courseType = courseType != null ? courseType.trim().toUpperCase() : "THEORY";
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title.trim() : "";
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType != null ? courseType.trim().toUpperCase() : "THEORY";
    }

    @Override
    public String toString() {
        return String.format("%-10s | %-35s | Credits: %d | Type: %-10s", courseCode, title, credits, courseType);
    }
}
